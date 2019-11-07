<!-- jQuery  -->
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js" integrity="sha384-UO2eT0CpHqdSJQ6hJty5KVphtPhzWj9WO1clHTMGa3JDZwrnQq4sF86dIHNDz0W1" crossorigin="anonymous"></script>
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

        var data;

        if($('#themeCSS').attr('href') == '/assets/css/global.css')
        {
            data = 0;
        }
        else
        {
            data = 1;
        }

        $.ajax({

            url : 'api/theme',
            method : 'POST',
            data : 'theme=' + data

        });

        location.reload();
        //reload

    });

    $('#dropMenuButton').click(function () {

        $('#dropMenu').toggleClass('show');

    });

</script>
<style>
    .fr-wrapper div a {
        display: none !important;
    }
</style>
