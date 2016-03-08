var gulp = require('gulp');
var jshint = require('gulp-jshint');
var concat = require('gulp-concat');
var uglify = require('gulp-uglify');
var rename = require('gulp-rename');

var source = 'src/main/web/';
var target = 'site/';

gulp.task('jslint', function () {
    return gulp.src(source + 'js/*.js')
        .pipe(jshint())
        .pipe(jshint.reporter('default'));
});

gulp.task('jsmin', function () {
    return gulp.src(source + 'js/*.js')
        .pipe(concat('all.js'))
        .pipe(gulp.dest(target + 'js'))
        .pipe(rename('all.min.js'))
        .pipe(uglify())
        .pipe(gulp.dest(target + 'js'));
});

gulp.task('rootdist', function () {
    return gulp.src(source + '*.*')
        .pipe(gulp.dest(target));
});

gulp.task('jsdist', function () {
    return gulp.src(source + 'js/*.js')
        .pipe(gulp.dest(target + 'js'));
});

gulp.task('imgdist', function () {
    return gulp.src(source + 'img/*.*')
        .pipe(gulp.dest(target + 'img'));
});

gulp.task('cssdist', function () {
    return gulp.src(source + 'css/*.css')
        .pipe(gulp.dest(target + 'css'));
});

gulp.task('watch', function () {
    gulp.watch(source + '*.*', ['rootdist']);
    gulp.watch(source + 'js/*.js', ['jsdist']);
    gulp.watch(source + 'img/*.*', ['imgdist']);
    gulp.watch(source + 'css/*.css', ['cssdist']);
});

gulp.task('build', ['jsdist', 'imgdist', 'cssdist', 'rootdist']);
