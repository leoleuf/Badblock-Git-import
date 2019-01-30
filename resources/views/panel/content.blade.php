<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <!-- Tell the browser to be responsive to screen width -->
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="">
    <!-- Favicon icon -->
    <link rel="icon" type="image/png" sizes="16x16" href="/panel/assets/images/favicon.png">
    <title>Serveur MultiGames | Tableau de Bord</title>
    <!-- Bootstrap Core CSS -->
    <link href="/panel/assets/plugins/bootstrap/css/bootstrap.min.css" rel="stylesheet">
    <!-- chartist CSS -->
    <link href="/panel/assets/plugins/chartist-js/dist/chartist.min.css" rel="stylesheet">
    <link href="/panel/assets/plugins/chartist-js/dist/chartist-init.css" rel="stylesheet">
    <link href="/panel/assets/plugins/chartist-plugin-tooltip-master/dist/chartist-plugin-tooltip.css" rel="stylesheet">
    <!--This page css - Morris CSS -->
    <link href="/panel/assets/plugins/c3-master/c3.min.css" rel="stylesheet">
    <!-- Custom CSS -->
    <link href="/panel/css/style.css" rel="stylesheet">
    <!-- You can change the theme colors from here -->
    <link href="/panel/css/colors/blue.css" id="theme" rel="stylesheet">
    <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
    <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
    <script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->
</head>

<body class="fix-header fix-sidebar card-no-border">
<div class="preloader">
    <svg class="circular" viewBox="25 25 50 50">
        <circle class="path" cx="50" cy="50" r="20" fill="none" stroke-width="2" stroke-miterlimit="10" /> </svg>
</div>
<div id="main-wrapper">
    <header class="topbar">
        <nav class="navbar top-navbar navbar-toggleable-sm navbar-light">
            <div class="navbar-header">
                <a title="Serveur MultiGames" class="navbar-brand" href="/">
                    <b>
                        Serveur MultiGames
                    </b>
                </a>
            </div>
            <div class="navbar-collapse">
                <ul class="navbar-nav my-lg-0">
                    <li class="nav-item dropdown">
                        <a title="Menu" class="nav-link dropdown-toggle text-muted waves-effect waves-dark" href="" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false"><img src="https://minotar.net/avatar/{{ Auth::user()->name }}/64.png" alt="user" class="profile-pic m-r-10" />{{ Auth::user()->name }}</a>
                    </li>
                </ul>
            </div>
        </nav>
    </header>
    <aside class="left-sidebar">
        <div class="scroll-sidebar">
            <nav class="sidebar-nav">
                <ul id="sidebarnav">
                    <li>
                        <a title="Mettre en avant mon serveur" class="waves-effect waves-dark" href="/dashboard/mise-en-avant" aria-expanded="false" style="color: red;"><i class="mdi mdi-nfc"></i><span class="hide-menu">Prendre la<br />première place</span></a>
                    </li>
                    <li>
                        <a title="Ajouter mon serveur" class="waves-effect waves-dark" href="/dashboard/add-server" aria-expanded="false"><i class="mdi mdi-checkbox-marked-circle-outline"></i><span class="hide-menu">Ajouter mon serveur</span></a>
                    </li>
                    <li>
                        <a title="Accéder à mes serveurs" class="waves-effect waves-dark" href="/dashboard" aria-expanded="false"><i class="mdi mdi-gauge"></i><span class="hide-menu">Mes Serveurs</span></a>
                    </li>
                    <li>
                        <a title="Logs" class="waves-effect waves-dark" href="/dashboard/logs" aria-expanded="false"><i class="mdi mdi-database-plus"></i><span class="hide-menu">Logs</span></a>
                    </li>
                    @if (Auth::user()->is_admin == 1)
                        <li>
                            <a title="Admin" class="waves-effect waves-dark" href="/dashboard/admin" aria-expanded="false"><i class="mdi mdi-database-plus"></i><span class="hide-menu">Admin</span></a>
                        </li>
                    @endif
                </ul>
            </nav>
        </div>
        <div class="sidebar-footer">
            <!-- item--><a title="Se déconnecter" ref="" onclick="event.preventDefault(); document.getElementById('frm-logout').submit();" class="link" data-toggle="tooltip" title="Logout"><i class="mdi mdi-power"></i></a> </div>
        <form id="frm-logout" action="{{ route('logout') }}" method="POST" style="display: none;">
            {{ csrf_field() }}
        </form>
    </aside>
    <div class="page-wrapper">
        <div class="container-fluid">
            @yield('title')
            <div class="row">
                <body>
                @yield('content')
                </body>
            </div>
        </div>
    </div>
</div>

<script src="/panel/assets/plugins/jquery/jquery.min.js"></script>
<!-- Bootstrap tether Core JavaScript -->
<script src="/panel/assets/plugins/bootstrap/js/tether.min.js"></script>
<script src="/panel/assets/plugins/bootstrap/js/bootstrap.min.js"></script>
<!-- slimscrollbar scrollbar JavaScript -->
<script src="/panel/js/jquery.slimscroll.js"></script>
<!--Wave Effects -->
<script src="/panel/js/waves.js"></script>
<!--Menu sidebar -->
<script src="/panel/js/sidebarmenu.js"></script>
<!--stickey kit -->
<script src="/panel/assets/plugins/sticky-kit-master/dist/sticky-kit.min.js"></script>
<!--Custom JavaScript -->
<script src="/panel/js/custom.min.js"></script>
<!-- ============================================================== -->
<!-- This page plugins -->
<!-- ============================================================== -->
<!-- chartist chart -->
<script src="/panel/assets/plugins/chartist-js/dist/chartist.min.js"></script>
<script src="/panel/assets/plugins/chartist-plugin-tooltip-master/dist/chartist-plugin-tooltip.min.js"></script>
<!--c3 JavaScript -->
<script src="/panel/assets/plugins/d3/d3.min.js"></script>
<script src="/panel/assets/plugins/c3-master/c3.min.js"></script>

<script async defer src="/gtag.js"></script>
<script async defer>
    window.dataLayer = window.dataLayer || [];
    function gtag(){dataLayer.push(arguments);}
    gtag('js', new Date());

    gtag('config', 'UA-122426050-1');
</script>
<!-- Chart JS -->
</body>
@yield('after_script')

</html>
