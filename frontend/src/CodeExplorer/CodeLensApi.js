export function loadGraph(query, onResult) {
  fetch('http://localhost:8080/?query=file%20' + encodeURIComponent(query))
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

  fetch(`http://localhost:8080/node/${selectedFile.vid}`)
     .then(function (response) {
       return response.json();
     })
     .then(function (data) {
       onFileSelect({ast: data.ast, text: data.text})
     })

}

export function loadMetrics(param, query, onMetricsLoaded) {
  fetch(`http://localhost:8080/analytics/${param}?query=file%20${encodeURIComponent(query)}`)
     .then(function (response) {
       return response.json();
     })
     .then(function (data) {
       onMetricsLoaded(data)
     })
}