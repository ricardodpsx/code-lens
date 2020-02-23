import {appModelDef, tabItems} from "./appModel";
import {loadFile, loadFrequencyHistory, loadGraph, loadHistory, loadMetrics} from "./api"
import {createApp} from "./lib/appModelBuilder";

jest.mock('./api');

loadGraph.mockResolvedValue({})
loadFile.mockResolvedValue({})
loadMetrics.mockResolvedValue({})

describe("code explorer", () => {

  describe("query", () => {
    let {model: {query}} = createApp(appModelDef)

    it('can change the query', () => {
      query.setQuery("fun")
      expect(query.text).toEqual("fun")
    })

    it('can update results', async () => {
      loadGraph.mockResolvedValue({results: ["1", "2"], metricNames: ["a", "b"], codeTree: {rootVid: "1"}})

      query.setQuery("fun")

      await resolvePromises()

      expect(loadGraph).toHaveBeenCalledWith("fun")
      expect(query.results).toEqual(["1", "2"]);
      expect(query.metricNames).toEqual(["a", "b"]);
      expect(query.codeTree).toEqual({rootVid: "1"});
    })

  })

  describe("File Selection", () => {
    let {model: {query, selectedFile}} = createApp(appModelDef)

    query.updateResults({codeTree: {rootVid: "1", vertices: {"1": {type: "file", data: {fileName: "aFile.js"}}}}})
    loadFile.mockResolvedValue({ast: {rootVid: "2"}, text: "file contents"})

    it("can be selected", () => {
      selectedFile.selectFile("1")
      expect(selectedFile.fileVid).toEqual("1")
      expect(selectedFile.fileName).toEqual("aFile.js")
    })

    it("Can load contents", async () => {
      query.updateResults({codeTree: {rootVid: "1", vertices: {"1": {type: "file", data: {fileName: "aFile.js"}}}}})

      selectedFile.selectFile("1")
      await resolvePromises()
      expect(loadFile).toHaveBeenCalledWith("1")
      expect(selectedFile.text).toEqual("file contents")
      expect(selectedFile.ast).toEqual({rootVid: "2"})
    })

  })

  describe("Tabs", () => {
    let {model: {query, tabs}} = createApp(appModelDef)

    it("Switches to File contents when selecting a file node", async () => {
      query.updateResults(
         {
           codeTree: {
             rootVid: "1",
             vertices: {
               "1": {type: "file", data: {fileName: "aFile.js"}, relations: [{name: "children", to: "2"}]},
               "2": {type: "astNode", relations: [{name: "parent", to: "1"}]}
             }
           }
         })

      query.selectTreeNode("2")
      await resolvePromises()
      expect(tabs.activeTab).toEqual(tabItems.SelectedFile)
    })

    it("Switches to results when results are updated", async () => {
      query.updateResults(["1", "2"])
      expect(tabs.activeTab).toEqual(tabItems.SearchResultsTab)
    })
  })

  describe("Metrics", () => {
    let {model: {query, metrics}} = createApp(appModelDef)
    it("Can select metrics", async () => {
      let metricData = [{paramValue: 4, frequency: 3, nodes: ["1"]}]
      query.updateResults({metrics: ["a", "b"]})
      loadMetrics.mockResolvedValue(metricData)

      metrics.selectMetric("b")
      expect(metrics.selectedMetric).toEqual("b")

      await resolvePromises()
      expect(loadMetrics).toHaveBeenCalledWith("b", "")
      expect(metrics.data).toEqual(metricData)
    })
  })

  describe("Evolution", () => {
    let {model: {evolution, query}} = createApp(appModelDef)
    it("Can select evolution param", async () => {
      let evolutionOfParamData = [{commit: {id: "123"}, statistics: {mean: 0.5}}]
      let frequencyOfQueryData = [{commit: {id: "123"}, frequency: 1}]

      query.setQuery("fun")
      loadFrequencyHistory.mockResolvedValue(frequencyOfQueryData)
      loadHistory.mockResolvedValue(evolutionOfParamData)

      evolution.$init()
      console.info(evolution.toString())
      evolution.selectParam("a")

      await resolvePromises()

      expect(loadFrequencyHistory).toHaveBeenCalledWith("fun")
      expect(evolution.selectedParam).toEqual("a")
      expect(evolution.history).toEqual(evolutionOfParamData)
      expect(evolution.frequency).toEqual(frequencyOfQueryData)
    })
  })

})


export function resolvePromises() {
  return Promise.resolve()
}
