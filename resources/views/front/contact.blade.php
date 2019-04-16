@section('title', 'Contacter nous - Serveur MultiGames')
@section('description', 'Vous pouvez contacter l\'équipe de Serveur MultiGames à partir de cette page.')
@section('logometa', 'https://serveur-multigames.net/img/logo.png')
@section('canonical', 'https://serveur-multigames.net/contact')
@section('jquery', 'async defer')
@extends('front.index')
@php($noautoad = 1)
@section('content')
    <section class="lis-bg-light pb-5">
        <div class="container pt-5">
            <div class="row wow fadeInUp">
                <div class="col-12 col-sm-6">
                    <div class="page-title">
                        <h2>Contactez-nous</h2>
                        <p class="mb-0">Un problème, une question ? Contactez-nous depuis ce formulaire.</p>
                    </div>
                </div>
                <div class="col-12 col-sm-6 text-left text-sm-right">
                    <ol class="breadcrumb mb-0 pl-0 bg-transparent pb-0">
                        <li class="breadcrumb-item d-inline-block float-none"><a title="Serveur MultiGames" href="/" class="lis-light">Serveur MultiGames</a></li>
                        <li class="breadcrumb-item d-inline-block float-none active">Contact</li>
                    </ol>
                </div>
            </div>
        </div>
    </section>

    <section>
        <div class="container">
            <div class="row">
                <div class="col-12 col-lg-12 mb-5 mb-lg-0">
                    <div class="card lis-brd-light wow fadeInUp">
                        <div class="card-body p-4">
                            @if (session()->has('flash'))
                                @foreach(session('flash') as $messageData)
                                    <div class="alert alert-{{ $messageData['level'] }} {{ $messageData['important'] ? 'alert-important' : '' }}">
                                        {!! trans($messageData['message']) !!}
                                    </div>
                                @endforeach
                            @endif
                            <form class="row" method="post">
                                <div class="col-12 col-sm-6">
                                    <div class="form-group lis-relative">
                                        <input type="text" class="form-control border-top-0 border-left-0 border-right-0 rounded-0 pl-4" name="name" placeholder="Votre nom">
                                        <div class="lis-search"> <i class="fa fa-user lis-primary lis-left-0"></i> </div>
                                    </div>
                                </div>
                                <div class="col-12 col-sm-6">
                                    <div class="form-group lis-relative">
                                        <input type="text" class="form-control border-top-0 border-left-0 border-right-0 rounded-0 pl-4" name="email" placeholder="Votre adresse e-mail">
                                        <div class="lis-search"> <i class="fa fa-envelope lis-primary lis-left-0"></i> </div>
                                    </div>
                                </div>
                                <div class="col-12 col-sm-12">
                                    <div class="form-group lis-relative">
                                        <input type="text" class="form-control border-top-0 border-left-0 border-right-0 rounded-0 pl-4" name="subject" placeholder="Sujet">
                                        <div class="lis-search"> <i class="fa fa-drivers-license-o lis-primary lis-left-0"></i> </div>
                                    </div>
                                </div>
                                <div class="col-12 col-sm-12">
                                    <div class="form-group lis-relative mb-0">
                                        <textarea class="form-control border-top-0 border-left-0 border-right-0 rounded-0 pl-4" name="message" placeholder="Message"></textarea>
                                        <div class="lis-search"> <i class="fa fa-pencil lis-primary lis-left-0 lis-top-10"></i> </div>
                                    </div>
                                </div>

                                {{ csrf_field() }}

                                <div class="col-12 col-sm-12"> <input type="submit" class="btn btn-primary btn-default mt-3" value="Envoyer" /> </div>
                            </form>
                        </div>
                    </div><br />

                <h3 class="mb-30">Contacter l'équipe de Serveur MultiGames</h3>

                            Si vous souhaitez contacter l'équipe de Serveur MultiGames, que vous soyez à la recherche de partenaires, que vous soyez créateur de serveur de jeu ou simplement un utilisateur de notre site, vous êtes sur la bonne page.<br />
                            Nous répondons généralement aux messages dans les vingt quatre heures ouvrés après réception du message. Une réponse par e-mail sera donc adressée à l'adresse laissé dans le champ prévu à cet effet dans le formulaire
                            de contact de l'équipe de Serveur MultiGames.<br /><br />
                            Pour tout problème avec un serveur de jeu privé, une plateforme de jeu ou n'ayant pas rapport direct avec notre site Internet, nous vous invitons directement à contacter les acteurs en question. Si le sujet de la demande
                            n'est pas clair ou que le message ne convient pas à des règles simples en terme d'orthographe, de syntaxe et de compréhension, le message ne sera pas lu et sera effacé dès l'ouverture de celui-ci.<br /><br />
                            Nous vous conseillons ainsi de bien vouloir nous envoyer un message avec un minimum de sens. Afin d'éviter de nombreuses attentes par mail, il est possible que nous vous demandions de vous munir d'une autre
                            plateforme de communication (en réponse à votre message) comme Discord ou Skype pour traiter certaines demandes ou régler certains problèmes plus facilement. En attendant, nous vous remercions de votre visite sur notre
                            site et nous sommes ouverts à tous les messages concernant Serveur MultiGames ainsi que les services et les classements proposés.<br /><br />
                            <i>P.S : Sachez que nous n'avons aucune responsabilité concernant les publicités affichés par notre régie publicitaire sur le site ni sur les liens externes entrés par les serveurs. Nous ne sommes également pas responsables
                            du contenu entré par les utilisateurs ou de tout autre potentiel dommage. Pour ces questions, nous vous invitons à consulter directement nos <a title="Conditions générales d'utilisation du site" href="/cgu">Conditions Générales d'Utilisation</a>.<br />
                            Pour tout autre sujet qui concernerait une dénonciation sur un manquement à une règle de notre charte, nous prenons en compte les signalements et ils sont traités généralement en une semaine puisque ce sont des cas plus longs à régler.</i><br /><br />
                            Attention, remplissez bien le formulaire suivant. Aucun retour ni aucune confirmation des informations entrées sera demandée après avoir cliqué sur l'envoi du formulaire de contact.

                        Vos données ne sont utilisées qu'aux fins de réponse à votre message. Ils ne sont ni revendus, ni distribuées à des entités tierces ou à une quelconque partie externe à Serveur MultiGames. Nous nous engagons sur la confidentialité de votre message et de l'auteur de celui-ci. Les messages sont traités par les administrateurs du sites et restent libres d'accorder une réponse ou non au message en question.
                        Nous traitons généralement plus lentement les cas qui demandent réflexion ou les messages avec des questions plus complètes. Notre équipe s'efforce à répondre à tous les messages possibles pour assurer notre transparence et notre bonne communication à travers nos utilisateurs. Actuellement, <b>97%</b> des messages sont répondues en moins de <b>24H</b>. Pour les suggestions, la une partie est refusée pour des questions techniques et aussi de visibilité,
                        il ne faut pas s'attendre à une acceptation de toutes les idées pour des raisons évidentes de mise en place, de gestion des priorités et de nos axes de travail. Nous contacter à travers ce formulaire reste le meilleur moyen de communiquer entre les différentes parties Utilisatrices, Joueurs, Créateurs et Administrateurs.<br /><br />
                        Dernière mise à jour des informations utiles à l'envoi d'un message sur le forumaire de contact le <b>03/11/2018</b>. Il est conseillé de vérifier sa boîte mail régulièrement pour vérifier si vous avez reçu une réponse au message envoyé. Par ailleurs, il est possible que le mail tombe dans les spams, ce qui peut rendre le mail réponse moins visible. Nous vous conseillons de bien vérifier vos boîtes et vos réseaux sociaux, beaucoup de personnes qui pensent ne pas
                        avoir reçu de réponses n'ont pas bien regardé leurs messages répondus.
            </div>
        </div>
    </section>

@endsection
@section('after_script')
    <script async defer src='https://www.google.com/recaptcha/api.js'></script>

    <script async defer>
        function onSubmit(token) {
            document.getElementById("contact-button").disabled = true;
            document.getElementById("contact-button").style.backgroundColor="#c0392b";
            document.getElementById("contact-button").innerHTML="Envoi en cours...";
            document.getElementById("myForm").submit();
        }
    </script>
@endsection