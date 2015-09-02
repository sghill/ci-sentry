class gocd($version = '15.2.0-2248') {
  package { 'go-server':
    ensure => installed,
  }

  service { 'go-server':
    ensure  => running,
    enabled => true,
  }

  package { 'go-agent':
    ensure => installed,
  }

  service { 'go-agent':
    ensure  => running,
    enabled => true,
  }

  package { 'openjdk-7-jdk':
    ensure => installed,
    before => [Package["go-agent-${go_version}"], Package["go-server-${go_version}"]]
  }
}

include apt
include gocd

