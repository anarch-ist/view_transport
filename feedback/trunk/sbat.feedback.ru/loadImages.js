(function () {

    var pageHeight = document.documentElement.clientHeight;
    var scrollPosition;
    var n = 0;
    var xmlhttp;

    setInterval(function () {
        scroll();
    }, 250);

    setInterval(function () {
        scroll_delay();
    }, 3000);

    function scroll() {

        if (navigator.appName == "Microsoft Internet Explorer")
            scrollPosition = document.documentElement.scrollTop;
        else
            scrollPosition = window.pageYOffset;

        if ((getContentHeight() - pageHeight - scrollPosition) < 500) {

            if (window.XMLHttpRequest)
                xmlhttp = new XMLHttpRequest();
            else if (window.ActiveXObject)
                xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
            else
                alert("Извините! Ваш браузер не поддерживает XMLHTTP!");

            if (n <= 550) {
                var url = "getImages.php?n=" + n;

                xmlhttp.open("GET", url, true);
                xmlhttp.send();

                n += 15;

                xmlhttp.onreadystatechange = putImages;
            }
              else {
                url = "destroy.php";
                xmlhttp.open("GET", url, true);
            }
        }

    }

    function scroll_delay() {

        if (navigator.appName == "Microsoft Internet Explorer")
            scrollPosition = document.documentElement.scrollTop;
        else
            scrollPosition = window.pageYOffset;

        //   if ((getContentHeight() - pageHeight - scrollPosition) < 1000) {

        if (window.XMLHttpRequest)
            xmlhttp = new XMLHttpRequest();
        else if (window.ActiveXObject)
            xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
        else
            alert("Извините! Ваш браузер не поддерживает XMLHTTP!");

        if (n <= 550) {
            var url = "getImages.php?n=" + n;

            xmlhttp.open("GET", url, true);
            xmlhttp.send();

            n += 15;

            xmlhttp.onreadystatechange = putImages;
        }
        else {
            url = "destroy.php";

            xmlhttp.open("GET", url, true);
        }


    }

    function getContentHeight() {
        return Math.max(
            document.body.scrollHeight, document.documentElement.scrollHeight,
            document.body.offsetHeight, document.documentElement.offsetHeight,
            document.body.clientHeight, document.documentElement.clientHeight
        );
    }

    function putImages() {

        if (xmlhttp.readyState == 4) {
            if (xmlhttp.responseText) {
                var resp = xmlhttp.responseText.replace("\r\n", "");
                var files = resp.split(";");
                var j = 0;
                for (var i = 0; i < files.length; i++) {

                    if (files[i] != "") {
                        var imageElement = '<a class="fancybox" rel="group" href="img/' + files[i] + '"><img alt="" src="thumb/' + files[i] + '" /></a>';
                        document.getElementById("container").innerHTML += imageElement;
                        j++;

                        if (j == 5 || j == 10)
                            document.getElementById("container").innerHTML += '<br />';
                        else if (j == 15) {
                            j = 0;
                        }
                    }
                }
            }
        }
    }

    //<a class="fancybox" rel="group" href="big_image_1.jpg"><img src="small_image_1.jpg" alt="" /></a>
    //    <a class="fancybox" rel="group" href="big_image_2.jpg"><img src="small_image_2.jpg" alt="" /></a>
})();

