package { 'openjdk-7-jdk':
  ensure => installed,
}

package { 'git':
  ensure => installed,
}

package { 'go-server':
  ensure  => installed,
  require => [Package['openjdk-7-jdk'], Apt::Source['gocd']],
}

package { 'go-agent':
  ensure  => installed,
  require => [Package['openjdk-7-jdk'], Apt::Source['gocd']],
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
  repos    => 'Release',
  key      => {
    id     => '9A439A18CBD07C3FF81BCE759149B0A6173454C7',
    server => 'pgp.mit.edu',
  },
  notify   => Exec['apt_update'],
}
