@extends('layouts.app')
@section('header')
    <link rel="stylesheet" href="/assets/plugins/magnific-popup/dist/magnific-popup.css"/>
    <link href="/assets/plugins/toastr/toastr.min.css" rel="stylesheet" type="text/css" />
@endsection
@section('content')
    <div class="content-page">
        <div class="content">
            <div class="container">
                @if (count($data))
                    <h3 style="text-align: center">Sanctions données par <b>{{ $username }}</b></h3>
                    <div class="row">
                        <div class="col-lg-2">
                        </div>
                        <div class="col-lg-10">
                            <table class="table table-hover">
                                <thead class="thead-dark">
                                    <th>Type</th>
                                    <th>Date</th>
                                    <th>Raison</th>
                                </thead>
                                <tbody>
                                    @foreach ($data as $user)
                                        <tr>
                                            <td>{{ $user['type'] }}</td>
                                            <td>{{ $user['date'] }}</td>
                                            <td><?php if(empty($user['reason'])){ echo('Aucune'); }else{ echo($user['reason']); } ?></td>
                                        </tr>
                                    @endforeach
                                </tbody>
                            </table>
                        </div>
                        <div class="col-lg-2">
                        </div>
                    </div>
                @else
                    <div class="alert alert-danger">
                        <p><b>Erreur</b> Cet utilisateur n'existe pas ou n'a donné aucune sanction</p>
                    </div>
                @endif
            </div>
        </div>
    </div>
@endsection