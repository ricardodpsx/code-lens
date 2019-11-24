const baseUrl = `http://localhost:8080`;


export function loadGraph(query, onResult) {
  fetch(`${baseUrl}/?query=file%20` + encodeURIComponent(query))
     .then(function (response) {
       return response.json();
     })
     .then(function (data) {
       onResult({
         codeTree: data.codeTree,
         results: data.results,
         metricNames: data.analyticsParams
       })
     })
}

export function loadFile(selectedFile, onFileSelect) {

  fetch(`${baseUrl}/node/${selectedFile.vid}`)
     .then(function (response) {
       return response.json();
     })
     .then(function (data) {
       onFileSelect({ast: data.ast, text: data.text})
     })

}

export function loadMetrics(param, query, onMetricsLoaded) {
  fetch(`${baseUrl}/analytics/${param}?query=file%20${encodeURIComponent(query)}`)
     .then(function (response) {
       return response.json();
     })
     .then(function (data) {
       onMetricsLoaded(data)
     })
}

export function loadSmells(onSmellsLoaded) {
  fetch(`${baseUrl}/smell/`)
     .then(function (response) {
       return response.json();
     })
     .then(function (data) {
       onSmellsLoaded(data)
     })
}

