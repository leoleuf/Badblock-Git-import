@extends('layouts.app')

@section('content')
<div class="row">
  <div class="col-12">
    <div class="card-box">
      @foreach ($user as $key => $data)
      <h4 class="m-t-0 header-title">Avertissement de {{ $data->pseudo }}</h4>
      <div class="row">
        <div class="col-12">
          Date : {{ $data->created_at }} / Avertis par : {{ $data->warn_by }} / Raison : 
          <br />
          {{ $data->text }}
          <div class="p-20">
          </div>
        </div>
      </div>
      @endforeach
    </div>
  </div>
</div>
@endsection
