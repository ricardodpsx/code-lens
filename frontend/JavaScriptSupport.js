let babelParser = require('@babel/parser');

const fs = require("fs")



const getTree = (content) => {
  const rawAst = babelParser.parse(content, {
    sourceType: 'module',
    plugins: ['jsx', 'classProperties', 'flow'],
  });


  return rawAst
};

let i = 0
while (process.argv[2 + i]) {
  try {
    const content = fs.readFileSync(process.argv[2 + i], 'utf8');
    const result = getTree(content);
    console.log(JSON.stringify(result));
  } catch (e) {
    console.log('{"program": {"body":[ ]} }')
  }
  i++
}


