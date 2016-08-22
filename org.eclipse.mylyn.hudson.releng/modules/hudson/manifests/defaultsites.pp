define hudson::defaultsites ($base = $hudson::base,) {
  /* Defaults */

  Hudson::Hudson {
    base => "$base",
  }

  Hudson::Site {
    data => "hudson-2.1",
    base => "$base",
  }

  /* Instances */

  hudson::hudson { "3.2.2":
    type      => "hudson",
    qualifier => "eclipse",
  }

  hudson::hudson { "3.3.3":
    type      => "hudson",
    qualifier => "eclipse",
  }

  hudson::hudson { "1.651.1":
    type      => "jenkins",
    qualifier => "stable",
  }

  hudson::hudson { "2.7.2":
    type      => "jenkins",
    qualifier => "stable",
  }

  /* Sites */


  hudson::site { "hudson-3.2.2":
    envtype => "hudson",
    version => "3.2.2",
    port    => 9322,
    require => Hudson["3.2.2"],
  }

  hudson::site { "hudson-3.3.3":
    envtype => "hudson",
    version => "3.3.3",
    port    => 9333,
    envdefault => true,
    require => Hudson["3.3.3"],
  }

  hudson::site { "jenkins-1.651.1":
    envtype => "jenkins",
    version => "1.651.1",
    port    => 9651,
    require => Hudson["1.651.1"],
    folderPlugin => true,
  }

  hudson::site { "jenkins-2.7.2":
    envtype => "jenkins",
    version => "2.7.2",
    port    => 9273,
    envdefault => true,
    require => Hudson["2.7.2"],
    folderPlugin => true,
  }

}