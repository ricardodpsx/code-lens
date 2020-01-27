const baseUrl = `http://localhost:8080`;

export function loadGraph(query) {
  query = query || "file"
  return fetch(`${baseUrl}/?query=` + encodeURIComponent(query))
     .then(function (response) {
       return response.json();
     })
     .then(function (data) {
       return {
         codeTree: data.codeTree,
         results: data.results,
         metricNames: data.analyticsParams
       }
     })
}

export function loadFile(selectedFile) {
  return fetch(`${baseUrl}/node/${encodeURIComponent(selectedFile)}`)
     .then(function (response) {
       return response.json();
     })
     .then(function (data) {
       return {ast: data.ast, text: data.text}
     })
}

export function loadMetrics(param, query) {
  return fetch(`${baseUrl}/analytics/${param}?query=${encodeURIComponent(query)}`)
     .then(function (response) {
       return response.json();
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
  return fetch(`${baseUrl}/history/${selectedMetric}?query=` + encodeURIComponent(query)
     + '&maxCommits=' + encodeURIComponent(maxCommits)
  ).then(function (response) {
    return response.json();
  })
}

export function loadFrequencyHistory(query, maxCommits = 20) {
  return fetch(`${baseUrl}/frequency-evolution/?query=` + encodeURIComponent(query)
     + '&maxCommits=' + encodeURIComponent(maxCommits)
  ).then(function (response) {
    return response.json();
  })
}