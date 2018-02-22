
(function ($) {

	console.log('initiating jquery');

	var heatmap_legend_fixed = false;
	var heatmap_legend_oldY = null;

	var heatmap_legend_fixedB = false;
	var heatmap_legend_oldYB = null;

	function heatmap_legend_scroll(divToFix)
	{
	 var _div = document.getElementById(divToFix); 
	 var div_y = _div.offsetTop;
	 var doc_y = window.pageYOffset;
	 
	 // setup
	 if (heatmap_legend_oldY==null)
	   heatmap_legend_oldY=div_y;
	 
	 
	if ((doc_y>(div_y+190)) & !heatmap_legend_fixed)
	 {
	   _div.style.position="fixed";
	   _div.style.top="0px";
	   heatmap_legend_fixed=true;
	 }
	 
	 if ((doc_y<heatmap_legend_oldY) & heatmap_legend_fixed)
	 {
	   _div.style.position="absolute";
	   _div.style.top="";
	  heatmap_legend_fixed=false;
	 }
	}


	function heatmap_legend_scrollB(divToFix)
	{
	 var _div = document.getElementById(divToFix); 
	 var _dat = document.getElementById('heatmap-data');
	 var div_y = _div.offsetTop;
	 var doc_y = window.pageYOffset;
	 
	 // setup
	 if (heatmap_legend_oldYB==null)
	   heatmap_legend_oldYB=div_y;
	 
	 
	if ((doc_y>div_y) & !heatmap_legend_fixedB)
	 {
	   _div.style.position="fixed";
	   _div.style.top="0px";
	   _dat.style.paddingTop="210px";
	   heatmap_legend_fixedB=true;
	 }
	 
	 if ((doc_y<heatmap_legend_oldYB) & heatmap_legend_fixedB)
	 {
	   _div.style.position="";
	   _div.style.top="";
	   _dat.style.paddingTop="";
	  heatmap_legend_fixedB=false;
	 }
	}

	if (document.getElementById('heatmap-header')!=null)heatmap_legend_scrollB("heatmap-header");





    function initWindow() {
        /* if the body is not high enough, put the footer to the bottom of the window */
        var wrapperHeight  = parseInt($('#wrapper').innerHeight());
        var viewportHeight = parseInt(window.innerHeight);
        if (!viewportHeight) {
            /* later IE 9 */
            viewportHeight = parseInt(window.document.documentElement.clientHeight);
        }
        if (viewportHeight - 50 > wrapperHeight) {
            $('footer').addClass('footerToBottom');
        } else {
            $('footer').removeClass('footerToBottom');
        }
    }



    /* Window resize event */
    $(window).resize(function() {

        initWindow();

    });


    $(document).ready(function() {

        initWindow();

    });




 })(jQuery);


