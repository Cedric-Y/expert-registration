/**
 * 通用 js
 */
function checkCodeReload()
{
	document.getElementById("checkCodeImg").src = 
		document.getElementById("checkCodeSrc").value+ "&nocache=" + new Date().getTime();
}