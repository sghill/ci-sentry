include java
include git
include maven
include apt

package { 'go-server':
  ensure  => installed,
  require => Package['java'],
}

package { 'go-agent':
  ensure  => installed,
  require => Package['java'],
}

service { 'go-server':
  ensure  => running,
  enable  => true,
}

service { 'go-agent':
  ensure  => running,
  enable  => true,
}

Apt::Source <| |> -> Package <| |> -> Service <| |>
