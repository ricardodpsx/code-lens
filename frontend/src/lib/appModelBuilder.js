import {applyMiddleware, compose, createStore} from "redux";
import {get, isArray, isFunction, isPlainObject, isUndefined, merge, set} from "lodash"
import React from "react";

let toList = obj => isArray(obj) ? obj : (!obj) ? [] : [obj]

function allActions(appModelDef, context) {
  return Object.entries(appModelDef)
     .reduce((acc, [k, v]) => ({...acc, [k]: allModelActions(appModelDef, context, k)}), {})
}

function allModelActions(appModelDef, context, modelName) {
  return Object.entries(appModelDef[modelName])
     .filter(([k]) => !k.startsWith("$"))
     .reduce((acc, [k, v]) => ({
       ...acc, [k]:
          data => context.store.dispatch({type: modelName + k, data, modelName, interactionName: k})
     }), {})
}

function allStates(appModelDef, context) {
  return Object.entries(appModelDef)
     .reduce((acc, [k, v]) => ({...acc, [k]: context.store.getState()[k]}), {})
}

function getChangeValueString(lazyLoadProp, context) {
  return lazyLoadProp.onChangeOf.reduce((acc, path) => JSON.stringify(get(context.store.getState(), path)) + acc, "")
}

function createEffectsMiddleware(appModelDef, context, modelProxy, store, next, action) {

  let {modelName, interactionName, data} = action

  if (interactionName === "$init") {
    let res = next(action)
    let lazyLoadProp = appModelDef[modelName].$init
    if (context.lazyPrevValues[modelName] !== getChangeValueString(lazyLoadProp, context)) {
      lazyLoadProp.effect(merge({}, allStates(appModelDef, context), allActions(appModelDef, context, modelName)))
      context.lazyPrevValues[modelName] = getChangeValueString(lazyLoadProp, context)
    }
    return res
  }

  let result = next(action)

  let effects = toList(appModelDef[modelName][interactionName].effects)

  Object.entries(appModelDef).forEach(([dstModelName, dstModel]) => {
    let whenEffects = toList(get(dstModel, ["$when", modelName, interactionName], []))

    whenEffects.forEach(fn => fn(data, modelProxy, store.getState()))
  })

  effects.forEach(fn => fn(data, modelProxy))

  return result
}

function createActionProxy(appModelDef, context) {
  const modelActions = new Proxy({}, {
    get: function (context, prop) {
      return actionProxy(prop)
    }
  });


  function actionProxy(modelName) {
    check(!!appModelDef[modelName], `model '${modelName}' is not declared`)
    return new Proxy({}, {
      get: function (_, prop) {
        if (prop === "toString") return () => JSON.stringify(context.store.getState()[modelName])


        checkModel(context, appModelDef, modelName, prop)
        let isAction = !!appModelDef[modelName][prop]
        let modelState = get(context.store.getState(), [modelName, prop], null)
        if (isAction)
          return (data) => context.store.dispatch({type: modelName + prop, data, modelName, interactionName: prop})
        else
          return modelState
      }
    })
  }

  return modelActions
}


function checkModel(context, appModelDef, modelName, prop) {
  let modelState = context.store.getState()[modelName]
  let isAction = !!appModelDef[modelName][prop]

  check(modelState == null || isPlainObject(modelState), `Model values should be objects, ${modelName} is ${typeof modelState}`)
  check(!isUndefined(get(modelState, prop)) || isAction, `Not found '${modelName}.${prop}'`)
  check(!(!isUndefined(get(modelState, prop)) && isAction), `Conflict between action name and prop name'${modelName}.${prop}'`)
}

function check(assertion, message) {
  if (!assertion)
    throw new Error(message)
}

export function createApp(appModelDef) {
  let context = {lazyPrevValues: {}}; //late assingnment
  let modelProxy = createActionProxy(appModelDef, context);

  const composeEnhancers =
     typeof window === 'object' &&
     window.__REDUX_DEVTOOLS_EXTENSION_COMPOSE__ ?
        window.__REDUX_DEVTOOLS_EXTENSION_COMPOSE__({}) : compose;

  const effectMiddleware = store => next => action => createEffectsMiddleware(appModelDef, context, modelProxy, store, next, action)

  const enhancer = composeEnhancers(applyMiddleware(effectMiddleware));

  let intialState = {}
  Object.entries(appModelDef)
     .forEach(([k, v]) => {
       check(v.$default == null || isPlainObject(v.$default), "Model values should be objects")
       intialState[k] = v.$default
     })

  function rootReducer(state = intialState, {modelName, interactionName, data}) {
    if (!modelName) return state

    let interaction = appModelDef[modelName][interactionName]
    let reducer = isFunction(interaction) ? interaction : interaction.reducer
    if (!reducer) return state

    let nextState = reducer(data, state[modelName], state)

    let prevState = state[modelName]
    let res = isPlainObject(nextState) ? {...prevState, ...nextState} : nextState

    return {...state, [modelName]: res}
  }

  context.modelProxy = modelProxy
  context.store = createStore(rootReducer, intialState, enhancer)
  return {store: context.store, model: modelProxy}
}

export function r(reducer, ...effects) {
  return {
    reducer: reducer,
    effects: effects
  }
}