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


                    <li class="has_sub">
                        <a href="javascript:void(0);" class="waves-effect"><i class="zmdi zmdi-device-hub"></i><span class="label label-warning pull-right">150</span><span> Instances</span> </a>
                        <ul class="list-unstyled">
                            <li><a href="form-elements.html">General Elements</a></li>
                            <li><a href="form-advanced.html">Advanced Form</a></li>
                            <li><a href="form-validation.html">Form Validation</a></li>
                            <li><a href="form-wizard.html">Form Wizard</a></li>
                            <li><a href="form-fileupload.html">Form Uploads</a></li>
                            <li><a href="form-wysiwig.html">Wysiwig Editors</a></li>
                            <li><a href="form-xeditable.html">X-editable</a></li>
                        </ul>
                    </li>



                    <li>
                        <a href="inbox.html" class="waves-effect"><i class="zmdi zmdi-email"></i><span class="label label-purple pull-right">New</span><span> Mail </span></a>
                    </li>

                    <li class="has_sub">
                        <a href="javascript:void(0);" class="waves-effect"><i class="zmdi zmdi-collection-item"></i><span> Pages </span> <span class="menu-arrow"></span></a>
                        <ul class="list-unstyled">
                            <li><a href="page-starter.html">Starter Page</a></li>
                            <li><a href="page-login.html">Login</a></li>
                            <li><a href="page-register.html">Register</a></li>
                            <li><a href="page-recoverpw.html">Recover Password</a></li>
                            <li><a href="page-lock-screen.html">Lock Screen</a></li>
                            <li><a href="page-confirm-mail.html">Confirm Mail</a></li>
                            <li><a href="page-404.html">Error 404</a></li>
                            <li><a href="page-500.html">Error 500</a></li>
                        </ul>
                    </li>

                    <li class="has_sub">
                        <a href="javascript:void(0);" class="waves-effect"><i class="zmdi zmdi-layers"></i><span>Extra Pages </span> <span class="menu-arrow"></span></a>
                        <ul class="list-unstyled">
                            <li><a href="extras-projects.html">Projects</a></li>
                            <li><a href="extras-tour.html">Tour</a></li>
                            <li><a href="extras-taskboard.html">Taskboard</a></li>
                            <li><a href="extras-taskdetail.html">Task Detail</a></li>
                            <li><a href="extras-profile.html">Profile</a></li>
                            <li><a href="extras-maps.html">Maps</a></li>
                            <li><a href="extras-contact.html">Contact list</a></li>
                            <li><a href="extras-pricing.html">Pricing</a></li>
                            <li><a href="extras-timeline.html">Timeline</a></li>
                            <li><a href="extras-invoice.html">Invoice</a></li>
                            <li><a href="extras-faq.html">FAQ</a></li>
                            <li><a href="extras-gallery.html">Gallery</a></li>
                            <li><a href="extras-email-template.html">Email template</a></li>
                            <li><a href="extras-maintenance.html">Maintenance</a></li>
                            <li><a href="extras-comingsoon.html">Coming soon</a></li>
                        </ul>
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
