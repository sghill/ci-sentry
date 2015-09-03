include java
include git

package { 'go-server':
  ensure  => installed,
  require => [Package['java'], Class['apt::update']],
}

package { 'go-agent':
  ensure  => installed,
  require => [Package['java'], Class['apt::update']],
}

service { 'go-server':
  ensure  => running,
  enable  => true,
  require => Package['go-server'],
}

service { 'go-agent':
  ensure  => running,
  enable  => true,
  require => Package['go-agent'],
}

apt::source { 'gocd':
  location => 'https://dl.bintray.com/gocd/gocd-deb',
  release  => '/',
  repos    => '',
  key      => {
    id     => '9A439A18CBD07C3FF81BCE759149B0A6173454C7',
    server => 'pgp.mit.edu',
  },
  before   => [Package['go-agent'], Package['go-server']],
}
