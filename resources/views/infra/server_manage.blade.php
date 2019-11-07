@extends('layouts.app')
@section('content')
    <div class="row">
        <div class="col-12">
            <div class="card-box">
                <h4 class="header-title mt-0 m-b-30">Server : Bungee</h4>
                <div class="">
                    <h5>Maintenance : @if($data['maintenance']['state']) <strong
                                class="text text-success">Actif</strong> @else <strong class="text text-danger">Désactivé</strong> @endif
                    </h5>
                    <br />
                    {{ Form::open(array('url' => '/server/motd', "method" => "POST")) }}
                        <label for="motd">MOTD</label>
                        <p>Pour mettre des espaces en tout début de chaîne, insérez un caractère (n'importe lequel) puis les espaces et votre texte. Exemple : <code>" &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Test MOTD</code></p>
<<<<<<< HEAD
                        <p>Pour centrer un texte, utilisez la balise <c> devant le texte (centre toute la ligne)</p>
=======
>>>>>>> 847eb807a3fb1c439fb7e1c8a08431e7d087b4db
                        <input class="form-control" name="motd" value="{{ $data['description'] }}" id="motd">
                        <br />
                        <input type="submit" class="btn btn-primary" value="Enregistrer" />
                    {{ Form::close() }}
                    <br />
                    <h5>Joueurs en ligne : <strong>{{ $data['players']['online'] }}
                            /{{ $data['players']['max'] }}</strong></h5>
                </div>
            </div>
        </div>
    </div>
@endsection
@section('after_scripts')

    <script>
        $('#motd').keyup(function () {

            $.ajax({

                url: '/server/motd',
                method: 'POST',
                data: 'motd=' + $(this).val(),
                success: function (data) {
                    console.log(data);
                }

            });

        });
    </script>

@endsection