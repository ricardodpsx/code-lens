const baseUrl = `http://localhost:8080`;

export function loadGraph(query, onResult) {
  query = query || "file"
  fetch(`${baseUrl}/?query=` + encodeURIComponent(query))
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

  fetch(`${baseUrl}/node/${encodeURIComponent(selectedFile.vid)}`)
     .then(function (response) {
       return response.json();
     })
     .then(function (data) {
       onFileSelect({ast: data.ast, text: data.text})
     })
}

export function loadMetrics(param, query, onMetricsLoaded) {
  query = query || "file"
  fetch(`${baseUrl}/analytics/${param}?query=${encodeURIComponent(query)}`)
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

export function loadHistory(selectedMetric, query, onLoad, maxCommits = 20) {
  fetch(`${baseUrl}/history/${selectedMetric}?query=` + encodeURIComponent(query)
     + '&maxCommits=' + encodeURIComponent(maxCommits)
  )
     .then(function (response) {
       return response.json();
     })
     .then(function (data) {
       onLoad(data)
     })
}

export function loadFrequencyHistory(query, onLoad, maxCommits = 20) {
  fetch(`${baseUrl}/frequency-evolution/?query=` + encodeURIComponent(query)
     + '&maxCommits=' + encodeURIComponent(maxCommits)
  ).then(function (response) {
    return response.json();
  })
     .then(function (data) {
       onLoad(data)
     })
}