@extends('layouts.app')
@section('header')
    <link rel="stylesheet" href="/assets/plugins/magnific-popup/dist/magnific-popup.css"/>
    <link href="/assets/plugins/toastr/toastr.min.css" rel="stylesheet" type="text/css" />
@endsection
@section('content')
    <div class="content-page">
        <div class="content">
            <div class="container">
                <h1>CloudFlare</h1>
                <div class="col-lg-12">
                    <div class="card-box">
                        <div class="dropdown pull-right">
                            <a href="#" class="dropdown-toggle arrow-none card-drop" data-toggle="dropdown"
                               aria-expanded="false">
                                <i class="mdi mdi-dots-vertical"></i>
                            </a>
                            <div class="dropdown-menu dropdown-menu-right">
                                <!-- item-->
                                <a href="javascript:void(0);" class="dropdown-item">Action</a>
                                <!-- item-->
                                <a href="javascript:void(0);" class="dropdown-item">Another action</a>
                                <!-- item-->
                                <a href="javascript:void(0);" class="dropdown-item">Something else</a>
                                <!-- item-->
                                <a href="javascript:void(0);" class="dropdown-item">Separated link</a>
                            </div>
                        </div>
                        <h4 class="m-t-0 header-title">Action sur CloudFlare</h4>

                        <table class="table">
                            <thead class="thead-dark">
                            <tr>
                                <th>Nom</th>
                                <th>État</th>
                                <th>Action</th>
                            </tr>
                            </thead>
                            <tbody>
                            <tr>
                                <td>Purge du cache</td>
                                <td><span class="badge badge-success">Actif</span></td>
                                <td>
                                    <button class="btn btn-success" id="purgeAll">Lancer</button>
                                </td>
                            </tr>
                            </tbody>
                        </table>
                    </div>

                </div>
            </div>
        </div>
    </div>
@endsection
@section('after_scripts')
    <script src="/assets/plugins/toastr/toastr.min.js"></script>
    <script>
        function sendCloudFlareRequest(requestID) {
            $.ajax({

                URL: '/api/cloudflare/' + requestID,
                method: 'GET',
                success : function () {
                    toastr.success('CloudFlare', "Le cache CloudFlare a été purgé avec succès");
                },
                error : function (data) {
                    toastr.error('CloudFlare', "Une erreur est survenue.");
                }

            });
        }

        $('#purgeAll').click(function(){

            sendCloudFlareRequest('purge/all');

        });

    </script>
@endsection