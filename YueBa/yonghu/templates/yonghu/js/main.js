var i = 0;
function bianhuan1()
{
	document.getElementById("container").className = "cube";
	i =1;
}
function bianhuan2()
{
	document.getElementById("container").className = "sw";
	i = 2;
}
function bianhuan3()
{
	document.getElementById("container").className = "kk";
	i = 3;
}
window.onscroll=function()
{
	if($(document).scrollTop() > 50)
	{
		document.getElementById("header").className="h2";
	}
	else	
	{
		document.getElementById("header").className="h1";
	}
}
function bianhuan()
{
	if( i == 1)
	{
		bianhuan2();
	}
	else if( i == 2)
	{
		 bianhuan3();
	}
	else
	{
		bianhuan1();
	}
	theTimer=window.setTimeout("bianhuan()", 5000);
		
}
theTimer=window.setTimeout("bianhuan()", 5000);

function change()
{
	
	 var dp =  document.getElementById("regisAndlogin").style.display;
	if(dp == "block")
	{
		document.getElementById("regisAndlogin").style.display = "none";
	} 
	else
		{
			document.getElementById("regisAndlogin").style.display = "block";
		}
	//document.getElementById("regisAndlogin").style.visibility = "hidden"
}