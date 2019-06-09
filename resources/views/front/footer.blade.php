<?php

$creationDate = "2018";
$date = date("Y");

if ($date != $creationDate)
{
    $date = $creationDate."-".$date;
}

?>
@if (!isset($pubtest))
<script async src="https://cdn.kiwys.com/build/kiwys.min.js"></script>
<ins class="kiwys-ads" data-ad-slot="10594"></ins>
@endif
<section class="image-bg footer lis-grediant grediant-bt pb-0">
    <div class="background-image-maker"></div>
    <div class="holder-image"> <img src="/dist/images/bg3.jpg" alt="Catégories de jeu vidéo annexes" class="img-fluid d-none"> </div>
    <div class="container">
        <div class="row pb-5">
            <div class="col-12 col-md-8">
                <div class="row">
                    <div class="col-12 col-sm-6 col-lg-3 mb-4 mb-lg-0">
                        <h5 class="footer-head">Créateurs</h5>
                        <ul class="list-unstyled footer-links lis-line-height-2_5">
                            <li><i class="fa fa-angle-right pr-1"></i> <a title="Ajouter mon serveur sur Serveur-MultiGames" href="/add-server">Ajouter votre serveur</a></li>
                            <li><i class="fa fa-angle-right pr-1"></i> <a title="Mettre en place l'API sur son site" href="/api">Mise en place API</a></li>
                            @if(!Auth::user())
                                <li><i class="fa fa-angle-right pr-1"></i> <a title="Se connecter sur Serveur-MultiGames" href="/login">Se connecter</a></li>
                                <li><i class="fa fa-angle-right pr-1"></i> <a title="S'inscrire sur Serveur-MultiGames" href="/register">S'inscrire</a></li>
                            @else
                                <li><i class="fa fa-angle-right pr-1"></i> <a title="Accéder au Tableau de Bord" href="/dashboard">Tableau de Bord</a></li>
                                <li><i class="fa fa-angle-right pr-1"></i> <a title="Se déconnecter" href="/logout">Se déconnecter</a></li>
                            @endif
                        </ul>
                    </div>
                    <div class="col-12 col-sm-6 col-lg-4 mb-4 mb-md-0">
                        <h5 class="footer-head">Nos différents jeux</h5>
                        <ul class="list-unstyled footer-links lis-line-height-2_5">
                            @foreach(config('tag.cat') as $k)
                                <li><i class="fa fa-angle-right pr-1"></i> &nbsp;<a title="Liste des serveurs {{ $k }}" href="/{{ encname($k) }}"><img alt="Serveur {{ $k }}" src="/img/{{ encname($k) }}.png" width="24" height="24" class="ialign" /> &nbsp;{{ ucfirst($k) }}</a></li>
                            @endforeach
                        </ul>
                    </div>
                    <div class="col-12 col-sm-8 col-lg-4">
                        <h5 class="footer-head">Informations légales</h5>
                        <ul class="list-unstyled footer-links lis-line-height-2_5">
                            <li><i class="fa fa-angle-right pr-1"></i> <a title="Conditions d'utilisation" href="/tos">Conditions d'Utilisation</a></li>
                            <li><i class="fa fa-angle-right pr-1"></i> <a title="Foire aux questions" href="/faq">Foire aux Questions</a></li>
                            <li><i class="fa fa-angle-right pr-1"></i> <a title="Règlement" href="/reglement">Règlement</a></li>
                            <li><i class="fa fa-angle-right pr-1"></i> <a title="Nous contacter" href="/contact">Contact</a></li>
                            <li><i class="fa fa-angle-right pr-1"></i> <a title="VPN detect" href="https://ipwarner.com">Detect vpn</a></li>
                            <li><i class="fa fa-angle-right pr-1"></i> <a title="Importance des liens" href="/lien">Lien</a></li>
                        </ul>
                    </div>
                </div>
            </div>
            <div class="col-12 col-md-4">
                <div class="footer-logo">
                    <a title="Serveur MultiGames" href="/"><img src="/img/logo.png" alt="Serveur MultiGames" class="img-fluid" /></a>
                </div>
                <p class="my-4">
                    Serveur MultiGames est un site Internet de liste et de classement des serveurs de jeux. Ajoutez gratuitement un serveur ou trouvez votre serveur préféré.
                    <br />
                    @if (session()->has('online_count'))
                        {{ session()->get('online_count') }} utilisateurs en ligne
                    @endif
                    @if (!_bot_detected())
                        <br />
                        <IMG SRC="http://loga.hit-parade.com/logohp1.gif?site=a672036" Title="Hit-Parade des sites francophones" WIDTH="77" HEIGHT="15" BORDER="0">
                    @endif
                </p>
            </div>
        </div>
    </div>
    <div class="footer-bottom mt-5 py-4">
        <div class="container">
            <div class="row">
                <div class="col-12 col-md-6 text-center text-md-left mb-3 mb-md-0"> <span> &copy; {{ $date }} Serveur-MultiGames</span> </div>
            </div>
        </div>
    </div>
</section>
<!--End  Footer-->
<!-- Top To Bottom-->
<a title="Revenir en haut de la page" href="#" class="scrollup text-center lis-bg-primary lis-rounded-circle-50">
    <div class="text-white mb-0 lis-line-height-1_7 h3"><i class="icofont icofont-long-arrow-up"></i></div>
</a>
<!-- End Top To Bottom-->

<!-- End Login /Register Form-->
<!-- jQuery -->
<!-- End footer Area -->
<script data-pagespeed-no-defer src="/dist/js/plugins.min.js"></script>
@if (!isset($invote))
<script src="/dist/js/common.js"></script>
@endif

<script>
    /* <![CDATA[ */
    var superpwa_sw = {"url":"\/js/superpwa-sw.js"};
    /* ]]> */
    if ('serviceWorker' in navigator) {window.addEventListener('load', function() {	navigator.serviceWorker.register(superpwa_sw.url) .then(function(registration) { /*console.log('Service worker ready');*/ registration.update(); }) .catch(function(error) { console.log('Registration failed with ' + error); }); });}
</script>

<script async src="https://www.googletagmanager.com/gtag/js?id=UA-122426050-1"></script>
<script>
    window.dataLayer = window.dataLayer || [];
    function gtag(){dataLayer.push(arguments);}
    gtag('js', new Date());

    gtag('config', 'UA-122426050-1');
</script>

</body>
</html>