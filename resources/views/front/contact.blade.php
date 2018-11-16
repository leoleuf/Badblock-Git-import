@section('title', 'Contacter nous - Serveur MultiGames')
@section('description', 'Vous pouvez contacter l\'équipe de Serveur MultiGames à partir de cette page.')
@section('logometa', 'https://serveur-multigames.net/img/logo.png')
@section('canonical', 'https://serveur-multigames.net/contact')
@section('jquery', 'async defer')
@extends('front.index')
@section('content')
    <section class="banner-area relative" id="home">
        <div class="overlay overlay-bg"></div>
        <div class="container">
            <div class="row d-flex align-items-center justify-content-center">
                <div class="about-content col-lg-12">
                    <h1 class="text-white">
                        Contactez nous
                    </h1>
                    <h2 class="text-white">
                        Serveurs MultiGames
                    </h2><br />
                    <p class="text-white link-nav"><a title="Serveur MultiGames" href="/">Serveur MultiGames</a>  <span class="lnr lnr-arrow-right"></span>  <a title="Contacter l'équipe" href="/contact">Contact</a></p>
                </div>
            </div>
        </div>
    </section>

    <div class="whole-wrap">
        <div class="container">
            <div class="section-top-border">
                <h3 class="mb-30">Contacter l'équipe de Serveur MultiGames</h3>
                <div class="row">
                    <div class="col-lg-12">
                        <p>
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
                        </p>
                        @if (session()->has('flash'))
                            @foreach(session('flash') as $messageData)
                                <div class="alert alert-{{ $messageData['level'] }} {{ $messageData['important'] ? 'alert-important' : '' }}">
                                    <a title="Contacter l'équipe" href="/contact" class="genric-btn primary radius" id="explicitbtn">{!! trans($messageData['message']) !!}</a>
                                </div>
                            @endforeach
                        @endif
                        <form class="form-area " id="myForm" action="/contact" method="post" class="contact-form text-right">
                            <div class="row">
                                <div class="col-lg-12 form-group">
                                    <input name="name" placeholder="Entrez votre pseudonyme ou nom complet" onfocus="this.placeholder = ''" onblur="this.placeholder = 'Entrez votre pseudonyme ou nom complet'" class="common-input mb-20 form-control" required="" type="text">

                                    <input name="email" placeholder="Entrez votre adresse e-mail utilisée pour la réponse" pattern="[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{1,63}$" onfocus="this.placeholder = ''" onblur="this.placeholder = 'Entrez votre adresse e-mail utilisée pour la réponse'" class="common-input mb-20 form-control" required="" type="email">

                                    <input name="subject" placeholder="Entrez le sujet de votre message" onfocus="this.placeholder = ''" onblur="this.placeholder = 'Entrez le sujet de votre message'" class="common-input mb-20 form-control" required="" type="text">
                                    <textarea class="common-textarea mt-10 form-control" name="message" placeholder="Message" onfocus="this.placeholder = ''" onblur="this.placeholder = 'Message'" required=""></textarea>
                                    <div class="mt-20 alert-msg" style="text-align: left;"></div>
                                    <button class="primary-btn mt-20 text-white g-recaptcha" id="contact-button"
                                            data-sitekey="6Lf8amQUAAAAAM2wJE-R24huo1IDSTgDQZVoURX1"
                                            data-callback="onSubmit" style="float: right;">Envoyer votre message</button>
                                    {{ csrf_field() }}
                                </div>
                            </div>
                        </form>
                        Vos données ne sont utilisées qu'aux fins de réponse à votre message. Ils ne sont ni revendus, ni distribuées à des entités tierces ou à une quelconque partie externe à Serveur MultiGames. Nous nous engagons sur la confidentialité de votre message et de l'auteur de celui-ci. Les messages sont traités par les administrateurs du sites et restent libres d'accorder une réponse ou non au message en question.
                        Nous traitons généralement plus lentement les cas qui demandent réflexion ou les messages avec des questions plus complètes. Notre équipe s'efforce à répondre à tous les messages possibles pour assurer notre transparence et notre bonne communication à travers nos utilisateurs. Actuellement, <b>97%</b> des messages sont répondues en moins de <b>24H</b>. Pour les suggestions, la une partie est refusée pour des questions techniques et aussi de visibilité,
                        il ne faut pas s'attendre à une acceptation de toutes les idées pour des raisons évidentes de mise en place, de gestion des priorités et de nos axes de travail. Nous contacter à travers ce formulaire reste le meilleur moyen de communiquer entre les différentes parties Utilisatrices, Joueurs, Créateurs et Administrateurs.<br /><br />
                        Dernière mise à jour des informations utiles à l'envoi d'un message sur le forumaire de contact le <b>03/11/2018</b>. Il est conseillé de vérifier sa boîte mail régulièrement pour vérifier si vous avez reçu une réponse au message envoyé. Par ailleurs, il est possible que le mail tombe dans les spams, ce qui peut rendre le mail réponse moins visible. Nous vous conseillons de bien vérifier vos boîtes et vos réseaux sociaux, beaucoup de personnes qui pensent ne pas
                        avoir reçu de réponses n'ont pas bien regardé leurs messages répondus.
                    </div>
                </div>
            </div>
            <div class="section-top-border">
            </div>
        </div>
    </div>
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