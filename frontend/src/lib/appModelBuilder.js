import {applyMiddleware, compose, createStore} from "redux";
import {get, isArray, isFunction, isPlainObject, isUndefined, merge, set, last} from "lodash"
import React from "react";

let toList = obj => isArray(obj) ? obj : (!obj) ? [] : [obj]

function createEffectsMiddleware(appModelDef, actionsProxy, store, next, action) {

  let {modelName, interactionName, data} = action

  let result = next(action)

  let effects = toList(appModelDef[modelName][interactionName].effects)

  Object.entries(appModelDef).forEach(([dstModelName, dstModel]) => {
    let whenEffects = toList(get(dstModel, ["$when", modelName, interactionName], []))

    whenEffects.forEach(fn => fn(data, actionsProxy, getState(appModelDef, store)))
  })

  effects.forEach(fn => fn(data, actionsProxy))

  return result
}

function createActionProxy(appModelDef, store) {
  const modelActions = new Proxy({}, {
    get: function (context, prop) {
      return actionProxy(prop)
    }
  });

  function actionProxy(modelName) {
    check(!!appModelDef[modelName], `model '${modelName}' is not declared`)
    return new Proxy({}, {
      get: function (_, prop) {
        if (prop === "toString") return () => JSON.stringify(getState(appModelDef, store)[modelName])

        //checkModel(store, appModelDef, modelName, prop)
        let isAction = !!appModelDef[modelName][prop]
        let modelState = getState(appModelDef, store, modelName, prop)
        if (isAction)
          return (data) => store.dispatch({type: modelName + prop, data, modelName, interactionName: prop})
        else
          return modelState
      }
    })
  }

  return modelActions
}


function checkModel(store, appModelDef, modelName, prop) {
  let modelState = store.getState()[modelName]
  let isAction = !!appModelDef[modelName][prop]

  check(modelState == null || isPlainObject(modelState), `Model values should be objects, ${modelName} is ${typeof modelState}`)
  check(!isUndefined(get(modelState, prop)) || isAction, `Not found '${modelName}.${prop}'`)
  check(!(!isUndefined(get(modelState, prop)) && isAction), `Conflict between action name and prop name'${modelName}.${prop}'`)
}

function check(assertion, message) {
  if (!assertion)
    throw new Error(message)
}


function buildReducer(appModelDef) {

  let initialState = {}
  Object.entries(appModelDef)
     .forEach(([k, v]) => {
       check(v.$default == null || isPlainObject(v.$default), "Model values should be objects")
       initialState[k] = v.$default
     })

  function rootReducer(state = initialState, {modelName, interactionName, data}) {
    if (!modelName) return state
    let subModelName = last(modelName.split("."))
    let interaction = appModelDef[subModelName][interactionName]
    let reducer = isFunction(interaction) ? interaction : interaction.reducer
    if (!reducer) return state

    let nextState = reducer(data, state[subModelName], state)

    let prevState = state[subModelName]
    let res = isPlainObject(nextState) ? {...prevState, ...nextState} : nextState

    return {...state, [subModelName]: res}
  }

  return {rootReducer}
}

function getState(appModelDef, store, modelName, prop) {
  if(!modelName) return store.getState()
  return get(store.getState(), [modelName, prop], null)
}

export function createApp(...appModelDefs) {
  let appModelDef = appModelDefs[0]

  if(!appModelDef.$name)
    appModelDef.$name = "model"

  const composeEnhancers =
     typeof window === 'object' &&
     window.__REDUX_DEVTOOLS_EXTENSION_COMPOSE__ ?
        window.__REDUX_DEVTOOLS_EXTENSION_COMPOSE__({}) : compose;

  let {rootReducer} = buildReducer(appModelDef);

  const effectMiddleware = store => {
    let actionsProxy = createActionProxy(appModelDef, store);
    return next => action => createEffectsMiddleware(appModelDef, actionsProxy, store, next, action)
  }

  const enhancer = composeEnhancers(applyMiddleware(effectMiddleware));
  let store = createStore(rootReducer, enhancer)
  let modelProxy = createActionProxy(appModelDef, store);

  return {store, model: modelProxy}
}

export function r(reducer, ...effects) {
  return {
    reducer: reducer,
    effects: effects
  }
}