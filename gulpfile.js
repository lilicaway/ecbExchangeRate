var gulp = require('gulp');
var del = require('del');
var vinylPaths = require('vinyl-paths');
var browserify = require('gulp-browserify');
var concat = require('gulp-concat');
var livereload = require('gulp-livereload');
var sourcemaps = require('gulp-sourcemaps');
var uglify = require('gulp-uglify');
var minifyCss = require('gulp-minify-css');

gulp.task('clean', [ 'cleanJs', 'cleanCss' ], function() {});

gulp.task('cleanJs', function() {
    return gulp.src('src/main/webapp/app/*js*').pipe(vinylPaths(del))
});

gulp.task('cleanCss', function() {
    return gulp.src('src/main/webapp/app/*css*').pipe(vinylPaths(del))
});

gulp.task('minifyAppCss', function() {
    return gulp.src([ 'src/main/webapp/WEB-INF/css/*.css' ])
        .pipe(sourcemaps.init())
        .pipe(minifyCss({processImport: false}))
        .pipe(sourcemaps.write('.'))
        .pipe(gulp.dest('./src/main/webapp/app'))
        .pipe(livereload());
});

gulp.task('copyBootstrapCss', [ 'minifyAppCss' ], function() {
    return gulp.src([ 'node_modules/bootstrap/dist/css/bootstrap.css' ])
        .pipe(sourcemaps.init())
        .pipe(minifyCss({processImport : false}))
        .pipe(sourcemaps.write('.'))
        .pipe(gulp.dest('./src/main/webapp/app'));
});

gulp.task('browserify', [ 'cleanJs' ], function() {
    return gulp.src([ 'src/main/javascript/app.js' ]).pipe(browserify({
      debug : true,
    })).pipe(sourcemaps.init())
    .pipe(concat('bundle.js'))
    .pipe(uglify())
    .pipe(sourcemaps.write('.'))
    .pipe(gulp.dest('./src/main/webapp/app'))
    .pipe(livereload());
  });

gulp.task('watch', function() {
    livereload.listen();
    gulp.watch('src/main/javascript/app.js', [ 'browserify' ]);
    gulp.watch('src/main/webapp/WEB-INF/css/*.css', [ 'minifyAppCss' ]);
});


gulp.task('dev', [
    'copyBootstrapCss',
//    'copyBootstrapFonts',
    'browserify',
    'watch'
    ]);

gulp.task('default', [
    'copyBootstrapCss',
//    'copyBootstrapFonts',
    'browserify'
    ]);
