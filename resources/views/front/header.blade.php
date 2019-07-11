<?php

$d = (isset($_SERVER['HTTPS']) && $_SERVER['HTTPS'] === 'on' ? "https" : "http") . "://serveur-multigames.net/".$_SERVER["REQUEST_URI"];

?><!DOCTYPE html>
<html lang="fr" class="no-js">
<head>
    <!--<base href="https://serveur-multigames.net">!-->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=5.0">

    <title>@yield('title')</title>
    @if (trim($__env->yieldContent('canonical')))<link rel="canonical" href="@yield('canonical')" />
    @endif
    @if (trim($__env->yieldContent('prev')))<link rel="prev" content="@yield('prev')">
    @endif
    @if (trim($__env->yieldContent('next')))<link rel="next" content="@yield('next')">
    @endif
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    @if (trim($__env->yieldContent('robots')))
        <meta name="robots" content="@yield('robots')" />
    @else
        <meta name="robots" content="follow, index, all" />
    @endif
    <meta name="description" content="@yield('description')" />
    <meta name="author" content="Philip Chalifour" />
    <meta name="keywords" content="serveur minecraft, minecraft, gratuit, serveur de jeu, serveur multigames" />

    <!--[if IE]><meta http-equiv="X-UA-Compatible" content="IE=edge"><![endif]-->

    <!-- Schema.org for Google -->
    <meta itemprop="name" content="@yield('title')">
    <meta itemprop="description" content="@yield('description')">
    <meta itemprop="image" content="@yield('logometa')">
    <!-- Twitter -->
    <meta name="twitter:card" content="summary">
    <meta name="twitter:title" content="@yield('title')">
    <meta name="twitter:description" content="@yield('description')">
    <meta name="twitter:site" content="@SMultigames">
    <meta name="twitter:creator" content="@SMultigames">
    <meta name="twitter:image" content="@yield('logometa')">
    <meta name="twitter:image:src" content="@yield('logometa')">
    @if (trim($__env->yieldContent('canonical')))<meta property="twitter:url" content="@yield('canonical')">
    @endif
    <!-- Open Graph general (Facebook, Pinterest & Google+) -->
    <meta property="og:title" content="@yield('title')">
    <meta property="og:description" content="@yield('description')">
    <meta property="og:image" content="@yield('logometa')">
    <meta property="og:image:width" content="54">
    <meta property="og:image:height" content="29">
    @if (trim($__env->yieldContent('canonical')))<meta property="og:url" content="@yield('canonical')">
    @endif
    <meta property="og:site_name" content="Serveur MultiGames">
    <meta property="og:locale" content="fr_FR">
    <meta property="og:type" content="website">

    @if (trim($__env->yieldContent('canonical')))
    <!--<link rel="alternate" href="@yield('canonical')" hreflang="x-default" />
    <link rel="alternate" href="@yield('canonical')" hreflang="fr" />
    <link rel="alternate" href="@yield('canonical')" hreflang="fr-fr" />!-->
    @endif

    @if (isset($catName) && $catName == "minecraft")
        <link rel="apple-touch-icon" sizes="180x180" href="/img/mc/apple-touch-icon.png">
        <link rel="icon" type="image/png" sizes="32x32" href="/img/mc/favicon-32x32.png">
        <link rel="icon" type="image/png" sizes="16x16" href="/img/mc/favicon-16x16.png">
        <link rel="manifest" href="/site.webmanifest">
        <link rel="mask-icon" href="/img/mc/safari-pinned-tab.svg" color="#5bbad5">
        <meta name="msapplication-TileColor" content="#da532c">
    @else
        <link rel="icon" type="image/png" sizes="16x16" href="/favicon-16x16.png">
        <link rel="icon" type="image/png" sizes="32x32" href="/favicon-32x32.png">
        <link rel="apple-touch-icon" sizes="180x180" href="/apple-touch-icon.png">
        <link rel="manifest" href="/site.webmanifest">
    @endif

    <meta name="theme-color" content="#2ecc71" />
    <meta name="google" content="notranslate">
    <script async src="https://pagead2.googlesyndication.com/pagead/js/adsbygoogle.js"></script>
    <script>
        (adsbygoogle = window.adsbygoogle || []).push({
            google_ad_client: "ca-pub-4636627444279583",
            enable_page_level_ads: true
        });
    </script>

    <link href="/dist/css/plugins.min.css" rel="stylesheet">
    <!--main Css-->
    <link href="/dist/css/main.min.css" rel="stylesheet">

    <script charset="UTF-8" src="//cdn.sendpulse.com/js/push/c350c0e7a65e7c7120017cee8cdc37a9_1.js" async></script>

</head>
<body>

<!-- header -->
<div id="header-fix" class="header fixed-top @if (isset($transperant)) transperant @endif">
    <nav class="navbar navbar-toggleable-md navbar-expand-lg navbar-light py-lg-0 py-4">
        <a title="Serveur MultiGames" class="navbar-brand mr-4 mr-md-5" href="/">
            <img src="/img/logo.png" alt="Logo Serveur MultiGames">
        </a>
        <div id="dl-menu" class="dl-menuwrapper d-block d-lg-none float-right">
            <button>Ouvrir le menu</button>
            <ul class="dl-menu">

                <a title="Accueil" class="nav-link" href="/" data-toggle="dropdown" aria-expanded="false">Accueil</a>
                <li> <a title="Mettre en avant son serveur de jeu" @if(Auth::user()) href="/dashboard/mise-en-avant" @else href="/mise-en-avant" @endif><i class="fa fa-plus pr-1"></i> Mise en avant</a></li>
                @if(!Auth::user())
                    <li> <a title="Me connecter" href="/login" class="text-white"><i class="fa fa-sign-in pr-2"></i> Connexion</a></li>
                    <li> <a title="M'inscrire" href="/register" class="text-white"><i class="fa fa-sign-in pr-2"></i> S'inscrire</a></li>
                @else
                    <li><a title="Accéder au tableau de bord" href="/dashboard" class="text-white"><i class="fa fa-dashboard"></i> Tableau de bord</a></li>
                    <li><a title="Déconnexion" href="/logout/{{ csrf_token() }}" class="text-white"><i class="fa fa-sign-in pr-2"></i> Se déconnecter</a></li>
                @endif




            </ul>
        </div>

        <div class="collapse navbar-collapse" id="navbarNav">
            <ul class="navbar-nav mr-auto">
                <li class="nav-item"><a title="Accueil" class="nav-link" href="/"><i class="fa fa-home"></i> Accueil</a></li>
                <li class="nav-item"><a title="Mettre en avant son serveur de jeu" class="nav-link" @if(Auth::user()) href="/dashboard/mise-en-avant" @else href="/mise-en-avant" @endif><i class="fa fa-star"></i> Mettre son serveur en avant</a></li>
            </ul>
            <ul class="list-unstyled my-2 my-lg-0">
                <li>
                    @if(!Auth::user())
                        <a title="Me connecter" href="/login" class="text-white"><i class="fa fa-sign-in pr-2"></i> Connexion</a>
                        &nbsp; | &nbsp;&nbsp;<a title="M'inscrire" href="/register" class="text-white"><i class="fa fa-sign-in pr-2"></i> S'inscrire</a>
                    @else
                        <a title="Accéder au tableau de bord" href="/dashboard" class="text-white"><i class="fa fa-dashboard"></i> Tableau de bord</a>
                        &nbsp; | &nbsp;&nbsp;
                        <a title="Déconnexion" href="/logout/{{ csrf_token() }}" class="text-white"><i class="fa fa-sign-in pr-2"></i> Se déconnecter</a>
                    @endif
                </li>
            </ul>
            <a title="Ajouter un serveur Minecraft" @if(Auth::user()) href="/dashboard/add-server" @else href="/add-server" @endif class="btn btn-outline-light btn-sm ml-0 ml-lg-4 mt-3 mt-lg-0"><i class="fa fa-plus pr-1"></i> Ajouter mon serveur</a>
        </div>
    </nav>
</div>