@php($transperant = 'true')
@section('title', 'Serveur MultiGames - Liste de Serveurs de jeu francophones')
@section('description', 'Découvrez les meilleurs serveurs MultiGames afin de trouver le meilleur serveur qui vous correspond et jouer avec tes amis gratuitement dessus. La liste est composée des derniers serveurs les plus populaires et les plus actifs.')
@section('logometa', 'https://serveur-multigames.net/img/logo.png')
@section('jquery', 'async defer')
@section('canonical', 'https://serveur-multigames.net/')
@section('noad', 'true')
@extends('front.index')
@section('content')

    <section class="image-bg lis-grediant grediant-tb">
        <div class="background-image-maker"></div>
        <div class="holder-image">
            <img src="/img/gaming.jpg" alt="Liste de serveurs de jeu" class="img-fluid d-none">
        </div>
        <div class="container">
            <div class="row justify-content-center pt-5">
                <div class="col-12 col-md-10 text-center wow fadeInUp">
                    <div class="heading pb-5">
                        <h1 class="display-4">Trouve ton serveur de jeu</h1>
                        <h4 class="font-weight-normal mb-0">Explore la liste des serveurs de jeu préférés de la communauté française.</h4>
                    </div>
                    </div>
                </div>
            </div>
        </div>
    </section>

    <section class="lis-grediant grediant-tb-white">
        <div class="container">

            <div class="row justify-content-center">
                <div class="col-12 col-md-10 text-center">
                    <div class="heading pb-4">
                        <h2 class="f-weight-500">Découvrez nos catégories de jeu</h2>
                        <h5 class="lis-light">Trouvez votre meilleur serveur.</h5>
                    </div>
                </div>
            </div>

            <div class="row">
                @php($m = 0)
                @foreach (config('tag.cat') as $k)
                    @if ($k != "Clash Of Clans")
                        <div class="col-12 col-sm-4 col-lg-2 wow fadeInUp mb-4 mb-lg-0 text-center">
                            <a title="Serveur {{ $k }}" href="/{{ encname($k) }}" class="text-white">
                                @php($m++)
                                <div class="lis-categories lis-bg{{ $m }} rounded lis-font-poppins py-4">
                                    <div class="text-white mb-2 h2">
                                        <img alt="Serveur {{ $k }}" src="img/{{ encname($k) }}.png" width="32" height="32">
                                    </div> {{ seocat($k) }}
                                </div>
                            </a>
                        </div>
                    @endif
                @endforeach
            </div>
        </div>
    </section>

    <section class="image-bg lis-grediant grediant-full">
        <div class="background-image-maker"></div>
        <div class="holder-image">
            <img src="dist/images/bg2.jpg" alt="" class="img-fluid d-none">
        </div>
        <div class="container">
            <div class="row justify-content-center">
                <div class="col-12 col-md-10 text-center">
                    <div class="heading pb-4">
                        <h2>Rejoignez notre classement</h2>
                        <h5 class="font-weight-normal">Découvrez tous les avantages que peuvent avoir notre liste</h5>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="col-12 col-md-4 text-center wow fadeInUp mb-5 mb-md-0">
                    <div class="lis-features-box lis-brd-light border rounded px-4 py-5">
                        <div  class="h1"><i class="fa fa-address-card-o h1"></i></div>
                        <h5>Une fiche précise</h5>
                        Donnez à la communauté envie de jouer à votre serveur avec une fiche détaillée et complète.
                    </div>
                </div>
                <div class="col-12 col-md-4 text-center wow fadeInUp mb-5 mb-md-0">
                    <div class="lis-features-box lis-brd-light border rounded px-4 py-5">
                        <div  class="h1"><i class="fa fa-bullhorn h1"></i></div>
                        <h5>Mettez en avant votre serveur</h5>
                        Montrez à la communauté la force de votre serveur en le mettant en avant.
                    </div>
                </div>
                <div class="col-12 col-md-4 text-center wow fadeInUp">
                    <div class="lis-features-box lis-brd-light border rounded px-4 py-5">
                        <div  class="h1"><i class="fa fa-handshake-o h1"></i></div>
                        <h5>Engagez-vous gratuitement</h5>
                        Inscrivez votre serveur gratuiterment sans frais et sans plus attendre.
                    </div>
                </div>
            </div>
        </div>
    </section>

    <section>
        <div class="container">
            <div class="row justify-content-center">
                <div class="col-12 col-md-10 text-center">
                    <div class="heading pb-4">
                        <h2>Comment ça fonctionne ?</h2>
                        <h5 class="font-weight-normal lis-light">Rejoindre notre plateforme en quelques étapes.</h5>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="col-12 col-md-4 text-center wow fadeInUp mb-5 mb-md-0">
                    <div class="icon-box box-line box-line-dotted1 lis-relative">
                        <img src="dist/images/icon-1.png" alt="" class="img-fluid mb-4 z-index-99  lis-relative" />
                        <h5>1. Inscrivez-vous</h5>
                        <p>Inscrivez-vous gratuitement sur notre plateforme et accédez à votre tableau de bord avec des statistiques complètes.</p>
                    </div>
                </div>
                <div class="col-12 col-md-4 text-center wow fadeInUp mb-5 mb-md-0">
                    <div class="icon-box box-line box-line-dotted2 lis-relative">
                        <img src="dist/images/icon-2.png" alt="" class="img-fluid mb-4 z-index-99  lis-relative" />
                        <h5>2. Ajoutez votre serveur</h5>
                        <p>Ajoutez votre serveur avec un nom, un logo et une description complète pour attirer la communauté sans plus attenedre.</p>
                    </div>
                </div>
                <div class="col-12 col-md-4 text-center wow fadeInUp mb-5 mb-md-0">
                    <div class="icon-box">
                        <img src="dist/images/icon-3.png" alt="" class="img-fluid mb-4 z-index-99  lis-relative" />
                        <h5>3. Reliez le système de vote à votre site</h5>
                        <p>Offrez des récompenses en échange avec notre API gratuite afin d'inciter vos joueurs à voter et à agrandir votre communauté.</p>
                    </div>
                </div>
            </div>
        </div>
    </section>

    <section class="lis-bg-primary py-5">
        <div class="container">
            <div class="row justify-content-center">
                <div class="col-12 col-md-6 text-center wow fadeInUp">
                    <div class="heading">
                        <h2 class="text-uppercase lis-line-height-1_5 text-white">VOUS SOUHAITEZ NOUS REJOINDRE GRATUITEMENT ?</h2>
                        <a href="/add-server" class="btn btn-outline-light btn-default text-uppercase"><i class="fa fa-plus pr-1"></i> Ajouter mon serveur</a>
                        <a title="Raccourcir un URL ou un lien" href="https://monlien.net" class="btn btn-outline-light btn-default text-uppercase">Raccourcir URL</a>
                    </div>
                </div>
            </div>
        </div>
    </section>
@endsection