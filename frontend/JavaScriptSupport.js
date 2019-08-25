let babelParser = require('@babel/parser');

const fs = require("fs")



const getTree = (content) => {
  const rawAst = babelParser.parse(content, {
    sourceType: 'module',
    plugins: ['jsx', 'classProperties', 'flow'],
  });


  return rawAst
};


const content = fs.readFileSync(process.argv[2], 'utf8');
const result = getTree(content);
console.log(JSON.stringify(result));

