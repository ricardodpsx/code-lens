const {openBrowser, goto, near, write, $, click, listItem, above, below, text, closeBrowser, dropDown} = require('taiko');
const assert = require("assert");

(async () => {
  try {
    await openBrowser();
    await goto("http://localhost:3000");
    await write("fun", $("input#search"));

    await click($("//li[contains(., 'js')]/span[1]"))
    await click($("//li[contains(., 'js')]//li[contains(.,'fixtures')]/span[1]"))
    await click($("//li[contains(.,'functions.js')]"))

    await click($("//span[contains(@class, 'code-entity-active')]//span[contains(text(), 'functionWith3Params')]"))

    await assert.ok(text("type: Identifier", near("firstLine: functionWith3Params")).exists())

    dropDown({name: "metricNames"}).select("lines")

    await assert.ok($("#reactgooglegraph-1").exists())

  } catch (error) {
    console.error(error);
  } finally {
    await closeBrowser();
  }
})();
