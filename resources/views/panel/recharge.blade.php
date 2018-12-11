@extends('panel.content')
@section('title')
    <div class="row page-titles">
        <div class="col-md-5 col-8 align-self-center">
            <h3 class="text-themecolor">Recharger mon solde</h3>
            <ol class="breadcrumb">
                <li class="breadcrumb-item"><a title="Serveur MultiGames" href="/">Serveur MultiGames</a></li>
                <li class="breadcrumb-item active">Recharger mon solde</li>
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
        <div class="col-lg-12 col-md-12">
            <div class="card">
                <div class="card-block">
                    <h3 class="card-title">Rechargez votre compte</h3>
                    <h6 class="card-subtitle">Effectuez l'opération de recharge dans cet encadré. L'opération ne prend que quelques secondes.</h6>
                </div>
                <div>
                    <hr class="m-t-0 m-b-0">
                </div>
                <div class="card-block">
                    <div class="row">
                        <div class="col-12">
                            <div data-dedipass="c7ff246fcf018c193859f4a52650f73a" data-dedipass-custom=""></div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

            <script src="//api.dedipass.com/v1/pay.js"></script>
@endsection