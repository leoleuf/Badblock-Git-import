function __fish_connect_needs_command
  set cmd (commandline -opc)
  if [ (count $cmd) -eq 1 -a $cmd[1] = 'connect' ]
    return 0
  end
  return 1
end

complete -f -c connect -n '__fish_connect_needs_command' -a list
for v in ( connect list )
        complete -f -c connect -n '__fish_connect_needs_command' -a "$v"
end
