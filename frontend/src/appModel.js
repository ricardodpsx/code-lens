import {createApp, r} from "./lib/appModelBuilder";
import {loadFile, loadFrequencyHistory, loadGraph, loadHistory, loadMetrics} from "./api";
import {fileAncestor, vdata, vertice} from "./lib/treeUtils";

export let tabItems = {
  SearchResultsTab: "SearchResultsTab",
  FrequenciesTab: "Frequencies",
  Graph: "Graph",
  SelectedFile: "Selected File",
  EvolutionOfMetric: "Evolution Metrics",
  EvolutionOfFrequency: "Evolution of Frequency"
}

export let appModelDef = {
  tabs: {
    $default: {items: tabItems, activeTab: tabItems.SearchResultsTab},
    selectTab: r((data) => ({activeTab: data})),
    $when: {
      query: {
        //updateResults: (_, {tabs: {selectTab}}) => selectTab(tabItems.SearchResultsTab),
        //filterResults: (_, {tabs: {selectTab}}) => selectTab(tabItems.SearchResultsTab)
        filterResults: (_, {tabs: {selectTab}}) => selectTab(tabItems.SearchResultsTab)
      }
    }
  },
  query: {
    $default: {text: "", codeTree: null, results: [], metricNames: [], selectedTreeNode: null},
    selectTreeNode: {
      reducer: selectedTreeNode => ({selectedTreeNode}),
      effects: (selectedTreeNode, {selectedFile: {selectFile}, query: {codeTree}}) => {
        if (!vertice(codeTree, selectedTreeNode)) return;
        let f = fileAncestor(codeTree, selectedTreeNode)
        if (f) selectFile(f)
      }
    },
    updateResults: r((results) => results),
    filterResults: r((results) => ({results})),
    setQuery: r(text => ({text}),
       (text, {query: {updateResults}}) => loadGraph(text)
          .then(({results, codeTree, metricNames}) => {
            updateResults({results, codeTree, metricNames})
          }))
  },
  selectedFile: {
    $default: {ast: null, text: "", fileName: null, fileVid: null, selectedNode: null},
    selectNode: selectedNode => ({selectedNode}),
    setFileContents: {
      reducer(data) {
        return {...data}
      },
      effects: (_, {query: {selectedTreeNode, results}}) => {
        let elem = (selectedTreeNode && document.getElementById(`code-entity-${selectedTreeNode}`))
        elem && elem.scrollIntoView({behavior: 'smooth'})
      }
    },
    selectFile: {
      reducer(fileVid, _, {query: {codeTree}}) {
        return {fileVid, fileName: vdata(codeTree, fileVid).fileName, selectedNode: fileVid}
      },
      effects: [
        (fileVid, {selectedFile: {setFileContents}}) => loadFile(fileVid).then(setFileContents),
        (fileVid, {tabs: {selectTab}}) => selectTab(tabItems.SelectedFile)
      ]
    }
  },
  metrics: {
    $default: {selectedMetric: "", data: []},
    updateMetrics: {
      reducer(data) {
        return {data}
      },
    },
    selectMetric: {
      reducer(selectedMetric) {
        return {selectedMetric}
      },
      effects: (selectedMetric, {metrics: {updateMetrics}, query: {text}}) =>
         loadMetrics(selectedMetric, text).then(updateMetrics)
    }
  },
  evolution: {
    $default: {selectedParam: "", frequency: [], history: []},
    selectParam: r(
       selectedParam => ({selectedParam}),
       (selectedParam, {evolution: {updateHistory}, query: {text}}) =>
          loadHistory(selectedParam, text).then(updateHistory)
    ),
    updateFrequency: frequency => ({frequency}),
    updateHistory: history => ({history}),
    $init: {
      effect: ({evolution: {updateFrequency}, query: {text}}) => {
        loadFrequencyHistory(text).then(updateFrequency)
      },
      onChangeOf: ["query.text"]
    }
  }
}


let {store, model} = createApp(appModelDef)
let {query, tabs, metrics, selectedFile, evolution} = model

export {store, model}
export let setQuery = query.setQuery
export let filterResults = query.filterResults
export let selectTab = tabs.selectTab
export let selectMetric = metrics.selectMetric
export let selectFile = selectedFile.selectFile
export let selectNodeInFile = selectedFile.selectNode
export let selectTreeNode = query.selectTreeNode
export let selectEvolutionParam = evolution.selectParam
export let loadFrequencyEvolution = evolution.$init