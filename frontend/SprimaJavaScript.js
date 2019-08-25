var esprima = require('esprima')

const fs = require("fs")



const getTree = (content) => {
  const rawAst = esprima.parseScript()


  return rawAst
};


const content = fs.readFileSync(process.argv[2], 'utf8');
const result = getTree(content);
console.log(JSON.stringify(result));

