<?php

$d = (isset($_SERVER['HTTPS']) && $_SERVER['HTTPS'] === 'on' ? "https" : "http") . "://$_SERVER[HTTP_HOST]$_SERVER[REQUEST_URI]";

?><!DOCTYPE html>
<html lang="fr" class="no-js">
<head>
    <base href="https://serveur-multigames.net">
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0">

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
        <link rel="icon" type="image/png" sizes="32x32" href="/img/icon32x32-minecraft.png">
    @else
        <link rel="icon" type="image/png" sizes="16x16" href="/favicon-16x16.png">
        <link rel="icon" type="image/png" sizes="32x32" href="/favicon-32x32.png">
        <link rel="apple-touch-icon" sizes="180x180" href="/apple-touch-icon.png">
        <link rel="manifest" href="/site.webmanifest">
    @endif

    <meta name="theme-color" content="#2ecc71" />
    <meta name="google" content="notranslate">

    @if (!isset($classement))
    <link rel="stylesheet" href="/css/bootstrap.css">
    <link rel="stylesheet" href="/css/main.css">
    @else
    <link rel="stylesheet" href="/css/classement.css">
    @endif
</head>
<body>

<header id="header" id="home">
    <div class="container">
        <div class="row align-items-center justify-content-between d-flex">
            <div id="logo">
                <a title="Serveur MultiGames" href="/"><img src="/img/logo.png" alt="Logo Serveur MultiGames" /></a>
            </div>
            <nav id="nav-menu-container">
                <ul class="nav-menu">
                    <li class="menu-active"><a title="Serveur MultiGames" href="/"><span class="lnr lnr-home"></span> &nbsp;Accueil</a>
                    <li>
                        <form method="post" action="https://serveur-multigames.net/recherche" class="recherche">
                            <input type="text" name="serveur" placeholder="&nbsp;&nbsp&nbsp&nbsp;Rechercher..." required class="single-input" id="barre-recherche">&nbsp;<div class="genric-btn success circle arrow" id="bouton-recherche"><span class="lnr lnr-magnifier" id="nomargin"></span></div>
                            {{ csrf_field() }}
                        </form>
                    </li>
                    @if(Auth::user())
                        <li><a class="ticker-btn" title="Tableau de Bord" href="/dashboard">Tableau de Bord</a></li>
                        <li><a class="ticker-btn" title="Se déconnecter" href="/logout/{{ csrf_token() }}">Déconnexion</a></li>
                    @else
                        <li><a class="ticker-btn" id="ajout-serveur" title="Ajouter son serveur MultiGames" href="/login"><span class="lnr lnr-plus-circle"></span> &nbsp;Ajouter mon serveur</a></li>
                    @endif
                </ul>
            </nav><!-- #nav-menu-container -->
        </div>
    </div>
</header><!-- #header -->

@if (!trim($__env->yieldContent('noad')))
<amp-auto-ads type="adsense"
              data-ad-client="ca-pub-1905923613312160">
</amp-auto-ads>
@endif
