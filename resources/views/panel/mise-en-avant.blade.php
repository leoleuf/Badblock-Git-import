@extends('panel.content')
@section('title')
    <div class="row page-titles">
        <div class="col-md-5 col-8 align-self-center">
            <h3 class="text-themecolor">Mettre en avant mon serveur</h3>
            <ol class="breadcrumb">
                <li class="breadcrumb-item"><a title="Serveur MultiGames" href="/">Serveur MultiGames</a></li>
                <li class="breadcrumb-item active">Mettre en avant mon serveur</li>
            </ol>
        </div>
    </div>
@endsection
@section('content')
        <!-- column -->

        @if (session()->has('flash'))
        <div class="col-lg-12">
                        @foreach(session('flash') as $messageData)
                            <div class="alert alert-{{ $messageData['level'] }} {{ $messageData['important'] ? 'alert-important' : '' }}">
                                @if(!$messageData['important'])
                                    <button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
                                @endif

                                {!! trans($messageData['message']) !!}
                            </div>
                        @endforeach
        </div>
        @endif
        <div class="col-lg-4 col-md-5">
            <div class="card">
                <div class="card-block">
                    <h3 class="card-title">Mon solde</h3>
                    <h6 class="card-subtitle">Solde restant exprimé en points</h6>
                </div>
                <div>
                    <hr class="m-t-0 m-b-0">
                </div>
                <div class="card-block text-center ">
                    <h1> {{ $data->credit }} points</h1>
                    <a href="/dashboard/recharge" class="btn btn-primary">+ Recharger</a>
                </div>
            </div>
                    <div class="card">
                                <div class="card-block">
                                    <h3 class="card-title">Prenez la première place !</h3>
                                    <h6 class="card-subtitle">Sélectionnez votre mise en avant</h6>
                                </div>
                                <div>
                                    <hr class="m-t-0 m-b-0">
                                </div>
                        <div class="card-block">
                            <div class="card-body">
                                @if(count($servers) > 0)
                                <form method="post">
                                    <div class="form-group">
                                        <label>Choisir votre serveur</label>
                                        <select name="server" class="form-control">
                                            @foreach($servers as $server)
                                            <option value="{{ $server->id }}">{{ seocat($server->cat) }} - {{ $server->name }}</option>
                                            @endforeach
                                        </select>
                                    </div>
                                    <div class="form-group">
                                        <label>Choisissez la date de mise en avant</label>
                                        <select name="date" class="form-control">
                                            @foreach($days as $k => $v)
                                            <option value="{{ $k }}">{{ $jour[date("w", strtotime($k))] }} {{ date("d", strtotime($k)) }} {{ $mois[date("n", strtotime($k))] }} {{ date("Y", strtotime($k)) }} @if ($k == $currentDate) (aujourd'hui) @endif - {{ $v['points'] }} points</option>
                                            @endforeach
                                        </select>
                                    </div>
                                    {{ csrf_field() }}
                                    <button type="submit" class="btn btn-info"><i class="mdi mdi-nfc"></i> Pousser à la première place</button>
                                </form>
                                @else
                                    <span color="red">Vous devez avoir ajouté un serveur sur votre compte pour prendre la première place.</span>
                                @endif
                            </div>
                        </div>
                    </div>
                </div>
        <div class="col-lg-8 col-md-7">
            <div class="card">
                <div class="card-block">
                    <h3 class="card-title">Comment la mise en avant fonctionne ?</h3>
                    <h6 class="card-subtitle">Informations concernant la mise en avant</h6>
                </div>
                <div>
                    <hr class="m-t-0 m-b-0">
                </div>
                <div class="card-block">
                    <div class="row">
                        <div class="col-12">
                            Votre serveur de jeu peut être mis en avant très simplement en sélectionnant le jour que vous souhaitez.<br /><br />
                            Le <strong>nombre de points</strong> du jour en question est calculé <strong>en fonction du nombre
                                de visites uniques et de nouveaux joueurs potentiels que peut vous rapporter votre mise en avant</strong> à la date donnée.
                            <br /><br />Nous proposons ce système dans le but de donner plus de visibilité aux
                            créateurs. Les tarifs sont très peu chers, en comparaison avec d'autres classements de serveurs.
                        </div>
                        <div class="col-12">
                        </div>
                    </div>
                </div>
            </div>
            <div class="card">
                <div class="card-block">
                    <h3 class="card-title">Serveurs mis en avant</h3>
                    <h6 class="card-subtitle">Liste de diffusion des annonces pour les serveurs</h6>
                </div>
                <div>
                    <hr class="m-t-0 m-b-0">
                </div>
                <div class="card-block">
                    <div class="row">
                        <div class="col-12">
                            @if(count($op) == 0)
                                Aucun serveur mis en avant pour cette période.
                            @else
                                <div class="table-responsive">
                                    <table class="table">
                                        <thead>
                                        <tr>
                                            <th>Type de serveur</th>
                                            <th>Nom du serveur</th>
                                            <th>Date</th>
                                        </tr>
                                        </thead>
                                        <tbody>
                                        @foreach($op as $row)
                                            <tr>
                                                <td><img alt="{{ seocat($row->server->cat) }}" src="/img/{{ encname($row->server->cat) }}.png" width="24" height="24" /> {{ seocat($row->server->cat) }}</td>
                                                <td>{{ $row->server->name }}</td>
                                                <td>{{ date_format(date_create($row->date), "d/m/Y") }} @if ($row->date == $currentDate) (aujourd'hui) @endif</td>
                                            </tr>
                                        @endforeach
                                        </tbody>
                                    </table>
                                </div>
                            @endif
                        </div>
                        <div class="col-12">
                        </div>
                    </div>
                </div>
            </div>
        </div>
@endsection