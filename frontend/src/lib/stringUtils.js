
export function formatDate(commitTime) {
  let monthNames = ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec']


  let date = new Date(commitTime)
  var day = date.getDate();
  var monthIndex = date.getMonth();
  let year = date.getFullYear()

  return `${day}/${monthNames[monthIndex]}/${year}`;
}