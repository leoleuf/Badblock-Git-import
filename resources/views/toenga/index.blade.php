@extends('layouts.app')
@section('content')
    <div class="content-page">
        <div class="content">
            <div class="col-md-6">
                <div class="card-box">
                    <h2 class="header-title m-t-0 m-b-30">Épic Server</h2>

                    <div id="ajaxTree1"></div>
                </div>
            </div><!-- end col -->
            <div class="col-md-6">
                <div class="card-box">
                    <h2 class="header-title m-t-0 m-b-30">Tower</h2>

                    <div id="ajaxTree2"></div>
                </div>
            </div><!-- end col -->
        </div><!-- end col -->
    </div><!-- end col -->

@endsection
@section("after_scripts")
    <link href="/assets/plugins/jstree/style.css" rel="stylesheet" type="text/css" />


    <script src="/assets/plugins/jstree/jstree.min.js"></script>

    <script>
        // Ajax
        $('#ajaxTree1').jstree({
            'core' : {
                'check_callback' : true,
                'themes' : {
                    'responsive': false
                },
                'data' : {
                    'url' : function (node) {
                        return node.id === '#' ? '/api/toenga/treeroot/epic' : '/api/toenga/treechild/epic/';
                    },
                    'data' : function (node) {
                        return { 'id' : node.id };
                    }
                }
            },
            "types" : {
                'default' : {
                    'icon' : 'fa fa-folder'
                },
                'file' : {
                    'icon' : 'fa fa-server'
                }
            },
            "plugins" : [ "contextmenu", "dnd", "search", "state", "types", "wholerow" ]
        });
    </script>

    <script>
        // Ajax
        $('#ajaxTree2').jstree({
            'core' : {
                'check_callback' : true,
                'themes' : {
                    'responsive': false
                },
                'data' : {
                    'url' : function (node) {
                        return node.id === '#' ? '/api/toenga/treeroot' : '/assets/plugins/jstree/ajax_children.json';
                    },
                    'data' : function (node) {
                        return { 'id' : node.id };
                    }
                }
            },
            "types" : {
                'default' : {
                    'icon' : 'fa fa-folder'
                },
                'file' : {
                    'icon' : 'fa fa-file'
                }
            },
            "plugins" : [ "contextmenu", "dnd", "search", "state", "types", "wholerow" ]
        });
    </script>


@endsection