@extends('panel.content')
@section('title')
    <div class="row page-titles">
        <div class="col-md-5 col-8 align-self-center">
            <h3 class="text-themecolor">Statistiques des votes</h3>
            <ol class="breadcrumb">
                <li class="breadcrumb-item"><a title="Serveur MultiGames" href="/">Serveur MultiGames</a></li>
                <li class="breadcrumb-item"><a title="Tableau de bord" href="/dashboard">Tableau de bord</a></li>
                <li class="breadcrumb-item active">Statistiques</li>
            </ol>
        </div>
    </div>
@endsection
@section('content')
    @if (!$data->verified)
        <div class="col-lg-12">
            <div class="card">
                <div class="card-block">
                    <h4 class="card-title">Vérifier la propriété {{ $data->name }}</h4>
                    @if (isset($err))
                        <div class="alert alert-danger">
                            {!! $err !!}
                        </div>
                    @endif
                    @if (empty($data->website) OR strlen($data->website) < 3)
                        Pour accéder à toutes les fonctionnalités des créateurs, votre serveur doit posséder un site Internet.<br />
                        Veuillez renseigner le site directement depuis les informations du serveur.<br />
                        <a class="btn btn-primary" title="Éditer mon serveur" href="/dashboard/edit-server/{{ $data->id }}">Éditer mon serveur</a>
                    @else
                        Afin d'accéder à toutes les fonctionnalités des créateurs, nos systèmes doivent vérifier la propriété du serveur. Cela ne prend que quelques secondes.<br /><br />

                        Veuillez placer le code suivant dans la page {{ $data->website }} pour vérifier que vous possédez bien ce serveur :<br />
                        <code style="background-color: #ecf0f1;">
                            &lt;a title=&quot;Serveur {{ seocat($data->cat) }}&quot; href=&quot;https://serveur-multigames.net/{{ encname($data->cat) }}&quot;&gt;Serveur {{ seocat($data->cat) }}&lt;/a&gt;
                        </code><br /><br />

                        Une fois le code placé sur cette page, cliquez sur le bouton de validation de la propriété.<br />
                        <font color="red">Attention : Ce lien peut être placé n'importe où sur la page, il permet de vérifier votre propriété mais également de nous sounteir.<br />
                        Afin que votre propriété reste valide, nous vous invitons à garder le lien.</font>
                        <form method="post">
                            {{ csrf_field() }}
                            <button type="submit" class="btn btn-primary">Valider la propriété</button>
                        </form>
                    @endif
                </div>
            </div>
        </div>
    @else
        <div class="col-lg-12">
            <div class="card">
                <div class="card-block">
                    <h4 class="card-title">Propriété {{ $data->name }}</h4>
                    Votre propriété est vérifiée. Vous pouvez accéder à toutes les fonctionnalités de ce serveur. <br />
                    <a class="btn btn-primary" title="Tableau de Bord de mon Serveur" href="/dashboard">Retour au tableau de bord</a>
                </div>
            </div>
        </div>
    @endif
@endsection