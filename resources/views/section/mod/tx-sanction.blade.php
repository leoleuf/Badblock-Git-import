@extends('layouts.app')
@section('header')
    <link rel="stylesheet" href="/assets/plugins/magnific-popup/dist/magnific-popup.css"/>
    <link href="/assets/plugins/toastr/toastr.min.css" rel="stylesheet" type="text/css" />
@endsection
@section('content')
    <div class="content-page">
        <div class="content">
            <div class="container">
                <h3 style="text-align: center">Émission de sanction</h3>
                <div class="card-box">

                    <h4 class="header-title m-t-0 m-b-30">Type de sanction</h4>

                    <button type="button" class="btn btn-primary waves-effect w-md waves-light m-b-5">Mute</button>
                    <button type="button" class="btn btn-success waves-effect w-md waves-light m-b-5">Ban</button>
                    <button type="button" class="btn btn-info waves-effect w-md waves-light m-b-5">Ban + Ban IP</button>
                    <button type="button" class="btn btn-warning waves-effect w-md waves-light m-b-5">Kick</button>
                    <button type="button" class="btn btn-danger waves-effect w-md waves-light m-b-5">Warn</button>
                    <br>
                    <br>
                    <h4 class="header-title m-t-0 m-b-30">Pseudo</h4>

                    <input type="text" name="pseudo" class="form-control" placeholder="Fluor">
                    <br>
                    <h4 class="header-title m-t-0 m-b-30">Raison</h4>

                    <select multiple="" name="raison" class="form-control">
                        @foreach($Raison as $row)
                            <option value="{{ $row }}">{{ $row }}</option>
                        @endforeach
                    </select>

                    <h4 class="header-title m-t-0 m-b-30">Temps</h4>

                    <br>
                    <input type="text" name="pseudo" class="form-control" placeholder="1d">


                </div>
            </div>
        </div>
    </div>
@endsection
@section('after_scripts')

@endsection
