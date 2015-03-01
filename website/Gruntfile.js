/*
 * wizard staff
 */

/*
 * GRUNT CONFIGURATION
 */
module.exports = function(grunt) {
  var html_files = {
    'dist/index.html' : 'src/index.html'
  };
  var css_files  = {
    'dist/css/main.css' : 'src/css/main.scss'
  };
  var js_files   = {
    'dist/js/main.js' : 'src/js/main.js'
  };

  // var img_copy =  {
  //   flatten : true,
  //   expand : true,
  //   cwd: 'src/img',
  //   src: ['**/*'],
  //   dest: 'dist/img/'
  // };

  /*
   * GRUNT INITIALIZATION CONFIGURATION
   */

  grunt.initConfig({

    /*
     * CONNECT
     *   live development server
     */
    connect: {
      server: {
        options: {
          base : 'dist',
          livereload : true,
          /* Choose Only 1 Hostname */
          // hostname : 'localhost' // local only
          hostname : '0.0.0.0' // viewable via LAN
        },
      }
    },

    /*
     * JSHINT
     *   making sure I don't write godawful JavaScript
     */
    jshint: {
      src: ['Gruntfile.js', 'src/js/*.js'],
      options: {
        curly : true,
        eqeqeq : true,
        immed : true,
        latedef : true,
        newcap : true,
        noarg : true,
        sub : true,
        undef : true,
        boss : true,
        eqnull : true,
        browser : true,
        globals : {
          require : true,
          define : true,
          requirejs : true,
          describe : true,
          expect : true,
          it : true,
          module  : true
        }
      }
    },

    /*
     * SASS
     *   the css preprocessor of choice
     */
    sass: {
      dev : {
        options : {
          style : 'expanded',
          trace : true,
          unixNewlines : true
        },
        files : css_files
      },
      dist : {
        options : {
          style : 'compressed',
          trace : true,
          unixNewlines : true
        },
        files : css_files
      }
    },

    /*
     * COPY
     *   for moving things around
     */
    copy : {
      dev : {
        files: [
          html_files,
          js_files,
          // img_copy,
        ]
      },
      dist : {
        files: [
          // img_copy,
        ]
      }
    },

    /*
     * CLEAN
     *   to clean stuff up.
     */
    clean: [".sass-cache", "dist"],

    /*
     * HTMLMIN
     *   for production minifying of HTML
     */
    htmlmin : {
      dist : {
        options : {
          removeComments: true,
          collapseWhitespace: true
        },
        files : html_files
      }
    },

    /*
     * UGLIFY
     *   for production minifying of JavaScript
     */
    uglify : {
      dist : {
        files : js_files,
        options : {
          mangle : false,

        }
      }
    },

    /*
     * WATCH
     *   for live reload server
     */
    watch : {
      options : {
        livereload : true,
      },
      html : {
        files : ['src/index.html'],
        tasks : ['copy:dev'],
      },
      js : {
        files : ['src/js/*.js'],
        tasks : ['jshint', 'copy:dev'],
      },
      css : {
        files : ['src/css/main.scss'],
        tasks : ['sass:dev'],
      },
      grunt : {
        files : ['Gruntfile.js'],
        tasks : ['jshint', 'copy:dev'],
      }
    }
  });

  /*
   * LOAD GRUNT TASKS
   */
  // Load JSHint Task
  grunt.loadNpmTasks('grunt-contrib-jshint');
  // Load Sass Compilation Task
  grunt.loadNpmTasks('grunt-contrib-sass');
  // Load Copy Tasks
  grunt.loadNpmTasks('grunt-contrib-copy');
  // Load Grunt Connect
  grunt.loadNpmTasks('grunt-contrib-connect');
  // Load Grunt Watch
  grunt.loadNpmTasks('grunt-contrib-watch');
  /* DIST ONLY TASKS */
  // Load HTML Minification Task
  grunt.loadNpmTasks('grunt-contrib-htmlmin');
  // Load JS Minification Task
  grunt.loadNpmTasks('grunt-contrib-uglify');
  /* CLEAN TASKS */
  grunt.loadNpmTasks('grunt-contrib-clean');

  /*
   * REGISTER GRUNT TASKS
   */
  // Default task builds a development distribution.
  grunt.registerTask('default', [
    'jshint',
    'sass:dev',
    'copy:dev'
  ]);

  // Dist builds a minified, production ready distribution.
  grunt.registerTask('dist', [
    'jshint',
    'sass:dist',
    'htmlmin',
    'uglify',
    'copy:dist'
  ]);

  // Runs a live reloading server specified by connect.
  grunt.registerTask('server', [
    'jshint',
    'sass:dev',
    'copy:dev',
    'connect',
    'watch'
  ]);

};
