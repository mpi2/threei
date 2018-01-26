
(function ($) {

	console.log('initiating jquery');

    function initFancybox() {
        /* Fancybox (popup) */
        //$('a[href$=".jpg"],.fancyframe').fancybox({'titlePosition':'inside','titleFormat':formatFancyboxTitle});
        //$('.fancybox').fancybox({'type':'image','titlePosition':'inside','titleFormat':formatFancyboxTitle});
//	$('a[href$=".jpg"],.fancyframe').fancybox({
//	    afterLoad: function() {
//	        this.title = '<a href="' + this.href +'">Download</a> ' + this.title;
//	    },
//	    helpers : {
//	        title: {
//	            type: 'inside'
//	        }
//	    }
//	});

        $('.fancybox').fancybox({
            'type': 'image',
            beforeLoad: function () {
                console.log('calling fancybox');
                var url = $(this.element).attr("fullRes");
                this.original = $(this.element).attr("original");
                this.fullRes = url;
            },
            afterLoad: function () {
                this.title = '<a href="' + this.fullRes + '"><i class="fa fa-download"></i> Download this image in high resolution</a>' + '&nbsp &nbsp <a href="' + this.original + '"><i class="fa fa-download"></i> Download this image in original format</a>' + this.title;
            },
            helpers: {
                title: {
                    type: 'inside'
                }
            }
        });

        $('.fancyboxGraph').fancybox({
            beforeLoad: function () {
                console.log('calling fancybox');
                var url = $(this.element).attr("href");
                console.log(url);
            },
            helpers: {
                title: {
                    type: 'inside'
                }
            }
        });
    }
 
 /* Document ready event */
 $(document).ready(function () {

     /* inits */

     initFancybox();
     

 });
 
 })(jQuery);