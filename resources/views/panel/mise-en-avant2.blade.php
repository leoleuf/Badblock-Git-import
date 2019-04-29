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

        <div class="col-lg-12 col-md-7">
            <div class="card">
                <div class="card-block">
                    <h3 class="card-title">Prenez la première place.</h3>
                    <div class="row">
                        <div class="col-12 mb-1">
                            <label>Quel serveur souhaite-tu mettre en avant ?</label>
                            <select name="server" class="form-control">
                                @foreach($servers as $server)
                                    <option value="{{ $server->id }}">{{ seocat($server->cat) }} - {{ $server->name }}</option>
                                @endforeach
                            </select><br />
                        </div>
                        <div class="col-12">
                            <label>Jours de mise en avant</label>
                            <div class="row">
                                @foreach($days as $k => $v)
                                    <div class="col-lg-3 col-md-2 mb-2">
                                        <div style="border-radius: 15px;
-moz-border-radius: 15px;
-webkit-border-radius: 15px;
border: 0.5px solid #000000; text-align: center; font-size:1.0rem;">
                                            <strong style="font-size:1.3rem;color:#000;">{{ $jour[date("w", strtotime($k))] }} {{ date("d", strtotime($k)) }} {{ $mois[date("n", strtotime($k))] }}</strong><br />
                                            {{ $v['place']+1 }}
                                            @if ($v['place'] == 0)ère
                                            @elseème
                                            @endif place
                                            <br />
                                            {{ number_format(round($v['points']/50, 2), 2, ',', '') }} €
                                        </div>
                                    </div>
                                @endforeach
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

@endsection