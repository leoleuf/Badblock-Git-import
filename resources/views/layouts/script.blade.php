<!-- jQuery  -->
<script src="/assets/js/jquery.min.js"></script>
<script src="/assets/js/bootstrap.min.js"></script>
<script src="/assets/js/detect.js"></script>
<script src="/assets/js/fastclick.js"></script>
<script src="/assets/js/jquery.slimscroll.js"></script>
<script src="/assets/js/jquery.blockUI.js"></script>
<script src="/assets/js/waves.js"></script>
<script src="/assets/js/jquery.nicescroll.js"></script>
<script src="/assets/js/jquery.slimscroll.js"></script>
<script src="/assets/js/jquery.scrollTo.min.js"></script>

<script src="/assets/plugins/switchery/switchery.min.js"></script>

<!-- KNOB JS -->
<script src="/assets/plugins/jquery-knob/jquery.knob.js"></script>

<!--Morris Chart-->
<script src="/assets/plugins/morris/morris.min.js"></script>
<script src="/assets/plugins/raphael/raphael-min.js"></script>

<!-- App js -->
<script src="/assets/js/jquery.core.js"></script>
<script src="/assets/js/jquery.app.js"></script>
<script>
    $('#changeTheme').click(function () {
        if($('#themeCSS').attr('href') == '/assets/css/global.css')
        {
            $('#themeCSS').attr("href", "/assets/css/global-dark.css");
            $('#infoTheme').html('(Blanc)');
        }
        else
        {
            $('#themeCSS').attr("href", "/assets/css/global.css");
            $('#infoTheme').html('(Noir)');
        }
    });

    $('#dropMenuButton').click(function () {

        $('#dropMenu').toggleClass('show');

    });

</script>

