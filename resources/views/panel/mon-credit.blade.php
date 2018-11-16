@extends('panel.content')
@section('title')
    <div class="row page-titles">
        <div class="col-md-5 col-8 align-self-center">
            <h3 class="text-themecolor">Mon crédit</h3>
            <ol class="breadcrumb">
                <li class="breadcrumb-item"><a title="Serveur MultiGames" href="/">Serveur MultiGames</a></li>
                <li class="breadcrumb-item"><a title="Tableau de bord" href="/dashboard">Dashboard</a></li>
                <li class="breadcrumb-item active">Mon crédit</li>
            </ol>
        </div>
    </div>
@endsection
@section('content')
   <!-- <div class="col-lg-4 col-md-5">
        <div class="card">
            <div class="card-block">
                <h3 class="card-title">Mon crédit : </h3>
                <h6 class="card-subtitle">Crédit restant exprimé en €</h6>
            </div>
            <div>
                <hr class="m-t-0 m-b-0">
            </div>
            <div class="card-block text-center ">
                <h1> {{ $data->credit }}€</h1>
                <button type="submit" class="btn btn-primary">+ Ajouter</button>
            </div>
        </div>
    </div>
    <div class="col-lg-8 col-md-7">
        <div class="card">
            <div class="card-block">
                <h3 class="card-title">Mes dernières opérations :</h3>
                <div class="row">
                    <div class="col-12">
                        <div class="table-responsive">
                            <table class="table">
                                <thead>
                                <tr>
                                    <th>Ref°</th>
                                    <th>Date</th>
                                    <th>Action</th>
                                    <th>Montant</th>
                                </tr>
                                </thead>
                                @if(count($op) == 0)
                                    Pas d'opérations !
                                @endif
                                <tbody>
                                @foreach($op as $row)
                                    <tr>
                                        <td>{{ $row->id }}</td>
                                        <td>{{ $row->date }}</td>
                                        <td>{{ $row->type }}</td>
                                        <td>{{ $row->amount }}</td>
                                    </tr>
                                @endforeach
                                </tbody>
                            </table>
                        </div>
                    </div>
                    <div class="col-12">
                    </div>
                </div>
            </div>
        </div>
    </div>!-->
   <div class="col-lg-12">
       <div class="card">
           <div class="card-block">
               <h4 class="card-title">Soon</h4>
               Le système de crédit arrive très prochainement !
           </div>
       </div>
   </div>
@endsection
@section('after_script')


@endsection