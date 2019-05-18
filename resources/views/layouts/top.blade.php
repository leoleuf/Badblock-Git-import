<body>

<!-- Navigation Bar-->
<header id="topnav">
    <div class="topbar-main">
        <div class="container-fluid">
            <div class="logo">

                <a href="/" class="logo">
                    <img src="https://cdn.badblock.fr/images/serveur-minecraft.png" alt="" height="40"
                         class="logo-small">
                    <h3>Manager Badblock</h3>
                    <img src="https://cdn.badblock.fr/images/serveur-minecraft.png" alt="" height="40"
                         class="logo-large">
                </a>
            </div>

            <div class="menu-extras topbar-custom">

                <ul class="list-unstyled topbar-right-menu float-right mb-0">

                    <li class="menu-item">
                        <!-- Mobile menu toggle-->
                        <a class="navbar-toggle nav-link">
                            <div class="lines">
                                <span></span>
                                <span></span>
                                <span></span>
                            </div>
                        </a>
                        <!-- End mobile menu toggle-->
                    </li>
                    <li class="hide-phone">
                        <form class="app-search" action="/players" method="get">
                            <input name="name" type="text" placeholder="Search..."
                                   class="form-control">
                            <button type="submit"><i class="fa fa-search"></i></button>
                        </form>
                    </li>
                    <li>
                        <!-- Notification -->
                        <div class="notification-box">
                            <ul class="list-inline mb-0">
                                <li>
                                    <a href="javascript:void(0);" class="right-bar-toggle">
                                        <i class="mdi mdi-bell-outline noti-icon"></i>
                                    </a>

                                    @if (DB::table('notifications')->where('user_id', "=", Auth::user()->id)->where('active', '=', '1')->count() != 0)
                                        <div class="noti-dot">
                                            <span class="dot"></span>
                                            <span class="pulse"></span>
                                        </div>
                                    @endif

                                </li>
                            </ul>
                        </div>
                        <!-- End Notification bar -->
                    </li>

                    <li class="dropdown notification-list show">
                        <a class="nav-link dropdown-toggle waves-effect waves-light nav-user" href="#" role="button"
                           aria-expanded="true" id="dropMenuButton">
                            <img src="https://cdn.badblock.fr/head/{{ Auth::user()->name}}/48.png" alt="user"
                                 class="rounded-circle">
                        </a>
                        <div class="dropdown-menu dropdown-menu-right profile-dropdown" id="dropMenu"
                             x-placement="bottom-end"
                             style="position: absolute; will-change: transform; top: 0px; left: 0px; transform: translate3d(-104px, 60px, 0px);">

                            <!-- item-->
                            <a href="/my-warns" class="dropdown-item notify-item">
                                <i class="ti-user m-r-5"></i> Mes Avertissements
                            </a>

                            <!-- item-->
                            <a href="/my-notifs" class="dropdown-item notify-item">
                                <i class="ti-settings m-r-5"></i> Mes Notifications
                            </a>

                            <!-- item-->
                            <a href="/logout" class="dropdown-item notify-item">
                                <i class="ti-power-off m-r-5"></i> Déconnexion
                            </a>

                        </div>
                    </li>

                </ul>
            </div>
            <!-- end menu-extras -->

            <div class="clearfix"></div>

        </div> <!-- end container -->
    </div>
    <!-- end topbar-main -->

    <div class="navbar-custom">
        <div class="container-fluid">
            <div id="navigation">
                <!-- Navigation Menu-->
                <ul class="navigation-menu">
                    @can('mod_find_user')
                        <li class="has-submenu">
                            <a href="/players"><i class="fa fa-search"></i> <span> Rechercher </span> </a>
                        </li>
                    @endcan
                    @can('staff_profil')
                        <li class="has-submenu">
                            <a href="#"><i class="fa fa-user"></i><span> Profile </span> </a>
                            <ul class="submenu">
                                <li><a href="/profil" class="waves-effect"><i class="fas fa-key"></i> Password</a>
                                </li>
                                <li><a href="/2fa" class="waves-effect"><i class="fas fa-lock"></i> A2F </a></li>
                                <li><a href="/hookix" class="waves-effect"><i class="fas fa-camera-retro"></i>
                                        HookiX </a></li>
                                <li><a href="/gallery" class="waves-effect"><i class="fas fa-images"></i> Ma galerie</a>
                                </li>
                                <li><a href="/profil/todolists" class="waves-effect"><i class="fa fa-list-ol"></i> Mes
                                        todolists </a></li>
                            </ul>
                        </li>
                    @endcan

                    @can('build_index')
                        <li class="has-submenu">
                            <a href="#"><i class="fas fa-cube"></i>Construction</a>
                            <ul class="submenu">
                                @can('build_upload_file')
                                    <li><a href="/profil/file-uploader" class="waves-effect"><i class="fa fa-file"></i>
                                            Transférer un fichier </a></li>
                                @endcan
                                @can('build_stats_connexion')
                                    <li>
                                        <a href="/section/build" class="waves-effect"><i class="fas fa-cube"></i>
                                            Stats Builders</a>
                                    </li>
                                @endcan
                                @can('build_project_view')
                                    <li>
                                        <a href="/build/project" class="waves-effect"><i class="fas fa-project-diagram"></i>
                                            Projets</a>
                                    </li>
                                @endcan
                            </ul>
                        </li>
                    @endcan

                    @can('mod_index')
                        <li class="has-submenu">
                            <a href="#"><i class="fa fa-briefcase"></i><span> Modération </span> </a>
                            <ul class="submenu">
                                @can('mod_proof')
                                    <li><a href="/section/preuves" class="waves-effect"><i
                                                    class="fas fa-camera-retro"></i>
                                            Preuves</a></li>
                                @endcan
                                @can("mod_center")
                                    <li><a href="/moderation" class="waves-effect"><i class="fa fa-bolt"></i> Mod Center</a>
                                    </li>
                                @endcan
                                @can('mod_search_mod_sanction')
                                    <li><a href="/moderation/sanction/search" class="waves-effect"><i class="fab fa-searchengin"></i> Rechercher Sanction</a></li>
                                @endcan
                                @can('mod_account_seen')
                                    <li><a disabled="" href="/moderation/seenaccount" class="waves-effect">
                                            <i class="fas fa-user-circle"></i> Account Seen</a></li>
                                @endcan
                                @can('mod_guardianer')
                                    <li><a disabled="" href="/moderation/guardian" class="waves-effect"><i
                                                    class="fa fa-legal"></i> Guardianer</a></li>
                                @endcan
                                @can('mod_stats_connexion')
                                    <li><a href="/section/connection" class="waves-effect"><i
                                                    class="fas fa-user-clock"></i>
                                            Connexion</a></li>
                                @endcan
                            </ul>
                        </li>
                    @endcan

                    @can('anim_index')
                        <li class="has-submenu">
                            <a href="#"><i class="fa fa-bomb"></i><span> Animation </span> </a>
                            <ul class="submenu">
                                @can('anim_give_pb')
                                    <li><a href="/animation/item" class="waves-effect"><i class="fa fa-gift"></i>
                                            Récompense(s)</a></li>
                                @endcan
                                @can('anim_give_item')
                                    <li><a href="/animation/pb" class="waves-effect"><i class="fa fa-coins"></i> Give PB</a>
                                    </li>
                                @endcan
                                @can('anim_send_automessages')
                                    <li><a href="/animation/msg-anim" class="waves-effect"><i
                                                    class="fas fa-comment-dots"></i> Message d'event </a></li>
                                @endcan
                            </ul>
                        </li>
                    @endcan

                    @can('redac_index')
                        <li class="has-submenu">
                            <a href="#"><i class="fa fa-pen"></i><span> Rédaction </span> </a>
                            <ul class="submenu">
                                @can('redac_stats_blog')
                                    <li><a href="/section/blog" class="waves-effect"><i class="fas fa-chart-bar"></i>
                                            Stats Articles</a></li>
                                @endcan
                                @can('redac_correct_view')
                                    <li><a href="/section/correction" class="waves-effect"><i
                                                    class="far fa-file-alt"></i>
                                            Correction de texte</a></li>
                                @endcan
                            </ul>
                        </li>
                    @endcan

                    @can('resp_index')
                        <li class="has-submenu">
                            <a href="#"><i class="fas fa-tools"></i><span> Responsables </span> </a>
                            <ul class="submenu">
                                @can('tools_notif')
                                    <li><a href="/section/notifications"><i class="fas fa-concierge-bell"></i>
                                            Notifications</a></li>
                                @endcan
                                @can('tools_warn')
                                    <li><a href="/section/avertissement-list"><i
                                                    class="fas fa-exclamation-triangle"></i> Avertissements</a></li>
                                @endcan
                                        <li><a href="/gradeperso-clear" class="waves-effect"><i class="fas fa-eraser"></i>
                                            Clear gradeperso</a></li>

                                @can('resp_paid_section')
                                    <li><a href="/section/paid" class="waves-effect"><i class="fa fa-bank"></i> Paies
                                            Sections</a></li>
                                @endcan
                                @can('resp_tfa_control')
                                    <li><a href="/section/tfacheck" class="waves-effect"><i class="fas fa-lock"></i>
                                            Controle TFA</a></li>
                                @endcan
                                @can('resp_staff_list')
                                    <li><a href="/section/allstaff" class="waves-effect"><i
                                                    class="fa fa-address-book"></i>
                                            Liste Staff</a></li>
                                    </li>
                                @endcan
                                @can('resp_todolist_all')
                                    <li><a href="/section/todo-management" class="wave-effect"><i
                                                    class="fa fa-list-ol"></i> Todo-List</a></li>
                                @endcan
                                @can('resp_validate_prefix')
                                    <li><a href="/website/prefix" class="waves-effect"><i class="fa fa-eye"></i> Préfix
                                        </a></li>
                                @endcan
                                @can('resp_vote_rewards')
                                    <li><a href="/website/vote" class="waves-effect"><i class="fa fa-envelope-open"></i>
                                            Votes </a></li>
                                @endcan
                            </ul>
                        </li>
                    @endcan

                    @can('website_index')
                        <li class="has-submenu">
                            <a href="#"><i class="fa fa-globe"></i> <span>Website </span> </a>
                            <ul class="submenu">
                                @can('tools_url_shorter')
                                    <li><a href="/section/url-shortener" class="waves-effect"><i
                                                    class="fab fa-shirtsinbulk"></i> Raccourcisseur d'URL</a></li>
                                @endcan
                                @can('resp_youtubers_list')
                                    <li><a href="/section/youtubers" class="waves-effect"><i class="fab fa-youtube"></i>
                                            Youtubers</a></li>
                                @endcan
                                @can('admin_manage_website')
                                    <li><a href="/website" class="waves-effect"><i class="fas fa-chart-line"></i>
                                            Analyse financière </a></li>
                                @endcan
                                @can('admin_manage_website')
                                    <li><a href="/website/shop" class="waves-effect"><i class="fa fa-dollar"></i>
                                            Gestion Boutique </a></li>
                                @endcan
                            </ul>
                        </li>
                    @endcan
                    @can('network_index')
                        <li class="has-submenu">
                            <a href="#"><i class="fa fa-network-wired"></i> <span>Infrastructure </span> </a>
                            <ul class="submenu">
                                @can('network_ddns')
                                    <li><a href="/infra/vrack" class="waves-effect"><i
                                                    class="fa fa-project-diagram"></i> DDNS Vrack </a></li>
                                @endcan
                                @can('network_docker')
                                    <li><a href="/infra/docker" class="waves-effect"><i class="fab fa-docker"></i>
                                            Docker </a></li>
                                @endcan
                                @can('network_console')
                                    <li><a href="/infra/console" class="waves-effect"><i class="fa fa-gear"></i> Console
                                        </a></li>
                                @endcan
                                @can('network_mongodb')
                                    <li><a href="/infra/mongodb" class="waves-effect"><i class="fas fa-database"></i>
                                            MongoDB </a></li>
                                @endcan
                                @can('network_cloudflare')
                                    <li><a href="/infra/cloudflare" class="waves-effect"><i
                                                    class="fab fa-cloudversify"></i>
                                            CloudFlare </a></li>
                                @endcan

                            </ul>
                        </li>
                    @endcan
                    @can('admin_index')
                        <li class="has-submenu">
                            <a href="#"><i class="fas fa-users-cog"></i> <span> Administration </span> </a>
                            <ul class="submenu">
                                @can('admin_manage_forum')
                                    <li><a href="/section/forum" class="waves-effect"><i class="fab fa-pied-piper"></i>
                                            Forum</a></li>
                                @endcan

                                @can('admin_user')
                                    <li><a href="/user" class="waves-effect"><i class="fa fa-address-book"></i>
                                            Utilisateur</a>
                                @endcan

                                @can('admin_role')
                                    <li><a href="/role" class="waves-effect"><i class="fa fa-list-ol"></i> Groupes</a>
                                    </li>
                                @endcan

                                @can('admin_perms')
                                    <li><a href="/permission" class="waves-effect"><i class="fa fa-gear fa-spin"></i>
                                            Permissions</a></li>
                                @endcan
                                @can('admin_server_perms')
                                    <li><a href="/section/permission-serv" class="waves-effect"><i
                                                    class="fas fa-terminal"></i> Permissions Serveur</a></li>
                                @endcan
                                @can('admin_server_manage')
                                    <li><a href="/server" class="waves-effect"><i class="fas fa-server"></i> Gestion du
                                            Serveur</a></li>
                                @endcan
                                @can('show_compta')
                                    <li><a href="/website/compta" class="waves-effect"><i
                                                    class="fas fa-file-invoice-dollar"></i> Comptabilité</a></li>
                                @endcan
                            </ul>
                        </li>
                    @endcan
                </ul>
            </div>
        </div>
    </div>
</header>
