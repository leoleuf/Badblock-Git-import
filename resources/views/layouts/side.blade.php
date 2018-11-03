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
                    <li>
                        <a href="/players" class="waves-effect">
                            <i class="fa  fa-search"></i>
                            <span> Rechercher</span>
                        </a>
                    </li>
                    <li class="has_sub">
                        <a href="javascript:void(0);" class="waves-effect"><i class="fa fa-user"></i> <span>Profile </span> <span class="menu-arrow"></span></a>
                        <ul class="list-unstyled">
                            <li><a href="/settings/sharex" class="waves-effect"><i class="fa fa-eye"></i><span> ShareX</span></a></li>
                            <li><a href="/screen" class="waves-effect"><i class="fa fa-eye"></i><span> Ma gallerie</span></a></li>
                        </ul>
                    </li>
                    @can('mod_index')
                        <li class="has_sub">
                            <a href="javascript:void(0);" class="waves-effect"><i class="fa fa-briefcase"></i> <span>Modération </span> <span class="menu-arrow"></span></a>
                            <ul class="list-unstyled">
                                <li><a href="/moderation" class="waves-effect"><i class="fa fa-bolt"></i><span> Mod Center</span></a></li>
                                <li><a href="/teamspeak/banlist" class="waves-effect"><i class="fa fa-legal"></i><span>TeamSpeak BanList</span></a></li>
                                <li><a disabled="" href="/" class="waves-effect"><i class="fa fa-legal"></i><span>Répartition</span></a></li>
                                <li><a disabled="" href="/" class="waves-effect"><i class="fa fa-legal"></i><span>Guardianer</span></a></li>
                                <li><a disabled="" href="/" class="waves-effect"><i class="fa fa-legal"></i><span>TX sanction</span></a></li>
                            </ul>
                        </li>
                    @endcan
                    @can('animation')
                        <li class="has_sub">
                            <a href="javascript:void(0);" class="waves-effect"><i class="fa fa-bomb"></i> <span>Animation </span> <span class="menu-arrow"></span></a>
                            <ul class="list-unstyled">
                                @can('animation_give')
                                    <li><a href="/animation/item" class="waves-effect"><i class="fa fa-gift"></i><span> Récompense(s)</span></a></li>
                                @endcan
                                @can('animation_givepb')
                                    <li><a href="/animation/pb" class="waves-effect"><i class="fa fa-coins"></i><span> Give PB</span></a></li>
                                @endcan
                            </ul>
                        </li>
                    @endcan
                    @can('gestion_index')
                        <li class="has_sub">
                            <a href="javascript:void(0);" class="waves-effect"><i class="fa fa-project-diagram"></i> <span>Gestion </span> <span class="menu-arrow"></span></a>
                            @can('gestion_forum')
                                <ul class="list-unstyled">
                                    <li><a href="/section/forum" class="waves-effect"><i class="fa fa-comment-o"></i><span> Forum</span></a></li>
                                </ul>
                            @endcan
                            @can('gestion_mod')
                                <ul class="list-unstyled">
                                    <li><a href="/" class="waves-effect"><i class="fa fa-comment-o"></i><span> Modération</span></a></li>
                                </ul>
                            @endcan
                            @can('gestion_forum')
                            @endcan
                            <ul class="list-unstyled">
                                <li><a href="/section/paid" class="waves-effect"><i class="fa fa-bank"></i><span> Paies Sections</span></a></li>
                                <li><a href="/section/tfacheck" class="waves-effect"><i class="zmdi zmdi-shield-security"></i><span>/!\ Controle TFA</span></a></li>
                                <li><a href="/section/allstaff" class="waves-effect"><i class="fa fa-address-book"></i><span>Liste Staff</span></a></li>
                                <li><a href="/user" class="waves-effect"><i class="fa fa-address-book"></i><span> Utilisateur</span></a></li>
                                <li><a href="/role" class="waves-effect"><i class="fa fa-list-ol"></i><span> Groupes</span></a></li>
                                <li><a href="/permission" class="waves-effect"><i class="fa fa-gear fa-spin"></i><span> Permissions</span></a></li>
                            </ul>
                        </li>
                    @endcan

                    @can('website')
                        <li class="has_sub">
                            <a href="javascript:void(0);" class="waves-effect"><i class="fa fa-globe"></i> <span>Website </span> <span class="menu-arrow"></span></a>
                            <ul class="list-unstyled">
                                @can('website_prefix')
                                    <li><a href="/website/prefix" class="waves-effect"><i class="fa fa-eye"></i><span> Préfix</span></a></li>
                                @endcan
                                @can('website_vote')
                                    <li><a href="/website/vote" class="waves-effect"><i class="fa fa-envelope-open"></i><span> Votes</span></a></li>
                                @endcan
                                @can('website_admin')
                                    <li><a href="/website" class="waves-effect"><i class="fa fa-dollar"></i><span> Administration</span></a></li>
                                @endcan

                            </ul>
                        </li>
                    @endcan
                </ul>
                <div class="clearfix"></div>
            </div>
            <!-- Sidebar -->
            <div class="clearfix"></div>
        </div>
    </div>
    <!-- Left Sidebar End -->