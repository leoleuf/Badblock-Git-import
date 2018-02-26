    <!-- ========== Left Sidebar Start ========== -->
    <div class="left side-menu">
        <div class="sidebar-inner slimscrollleft">

            <!-- User -->
            <div class="user-box">
                <div class="user-img">
                    <img src="https://guiria.badblock.fr/head/{{ Auth::user()->name}}/84.png" alt="user-img" title="{{ Auth::user()->name}}" class="img-circle img-thumbnail img-responsive">
                    <div class="user-status offline"><i class="zmdi zmdi-dot-circle"></i></div>
                </div>
                <h5><a href="#">{{ Auth::user()->name}}</a> </h5>
                <ul class="list-inline">
                    <li>
                        <a href="#" >
                            <i class="zmdi zmdi-settings"></i>
                        </a>
                    </li>

                    <li>
                        <a class="text-custom" href="{{ route('logout') }}"
                           onclick="event.preventDefault();
                           document.getElementById('logout-form').submit();">
                            <i class="zmdi zmdi-power"></i>
                        </a>

                        <form id="logout-form" action="{{ route('logout') }}" method="POST" style="display: none;">
                            {{ csrf_field() }}
                        </form>
                    </li>
                </ul>
            </div>
            <!-- End User -->

            <!--- Sidemenu -->
            <div id="sidebar-menu">
                <ul>
                    <li class="text-muted menu-title">Navigation</li>

                    <li>
                        <a href="/" class="waves-effect"><i class="zmdi zmdi-view-dashboard"></i> <span> Dashboard </span> </a>
                    </li>
                    <li>
                        <a href="/players" class="waves-effect"><i class="zmdi zmdi-view-dashboard"></i> <span> Dashboard </span> </a>
                    </li>
                    <li>
                        <a href="/toenga" class="waves-effect"><i class="zmdi zmdi-view-dashboard"></i> <span> Instances </span> </a>
                    </li>
                    <li>
                        <a href="/devhome" class="waves-effect"><i class="zmdi zmdi-view-dashboard"></i> <span> Développement </span> </a>
                    </li>
                    <li>
                        <a href="/website" class="waves-effect"><i class="zmdi zmdi-view-dashboard"></i> <span> Site </span> </a>
                    </li>
                    </li>


                    <li class="has_sub">
                        <a href="javascript:void(0);" class="waves-effect"><i class="zmdi zmdi-shield-security"></i> <span> Gestion </span> <span class="menu-arrow"></span></a>
                        <ul class="list-unstyled">
                            <li><a href="/role">Role</a></li>
                            <li><a href="/permission">Permissions</a></li>
                            <li><a href="/user">Utilisateurs</a></li>

                        </ul>
                    </li>

                </ul>
                <div class="clearfix"></div>
            </div>
            <!-- Sidebar -->
            <div class="clearfix"></div>

        </div>

    </div>
    <!-- Left Sidebar End -->
