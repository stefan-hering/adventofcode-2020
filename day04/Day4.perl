#!/usr/bin/perl
use strict;
use warnings;

use File::Slurp qw(read_file);

my $text = read_file('input');

# [ { 'byr' => '2001', ...},{ 'byr' => '1928', ...} ]
my @passports = map +{ split /[:\s]/, $_ },split /\n\n/, $text;

my @valid_keys = qw(byr iyr eyr hgt hcl ecl pid );
my @filledPassports = grep {
    my %passport = %$_;
    scalar (grep { exists $passport{$_} } @valid_keys) == 7;
} @passports;

my @validPassports = grep {
    $_->{byr} =~ /^(19[2-9][0-9])|(200[0-2])$/ &&
    $_->{iyr} =~ /^(201[0-9])|(2020)$/ &&
    $_->{eyr} =~ /^(202[0-9])|(2030)$/ &&
    $_->{hgt} =~ /^(1[5-8][0-9]cm)|(19[0-3]cm)|(59in)|(6[0-9]in)|(7[0-6]in)$/ &&
    $_->{hcl} =~ /^#[0-9a-f]{6}$/ &&
    $_->{ecl} =~ /^(amb)|(blu)|(brn)|(gry)|(grn)|(hzl)|(oth)$/ &&
    $_->{pid} =~ /^[0-9]{9}$/
} @filledPassports;

print scalar @filledPassports . "\n";
print scalar @validPassports . "\n";
