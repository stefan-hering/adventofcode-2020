#!/usr/bin/perl
use strict;
use warnings;

use File::Slurp qw(read_file);

my $text = read_file('input');

# [ { 'byr' => '2001', ...},{ 'byr' => '1928', ...} ]
my @passports = map +{ split /[:\s]/, $_ },split /\n\n/, $text;

my @valid_keys = ("byr", "iyr", "eyr", "hgt", "hcl", "ecl", "pid" );
my @filledPassports = grep {
    my %passport = %$_;
    scalar (grep { exists $passport{$_} } @valid_keys) == 7;
} @passports;

my @validPassports = grep {
    my %passport = %$_;
    $passport{"byr"} =~ /^(19[2-9][0-9])|(200[0-2])$/ &&
    $passport{"iyr"} =~ /^(201[0-9])|(2020)$/ &&
    $passport{"eyr"} =~ /^(202[0-9])|(2030)$/ &&
    $passport{"hgt"} =~ /^(1[5-8][0-9]cm)|(19[0-3]cm)|(59in)|(6[0-9]in)|(7[0-6]in)$/ &&
    $passport{"hcl"} =~ /^#[0-9a-f]{6}$/ &&
    $passport{"ecl"} =~ /^(amb)|(blu)|(brn)|(gry)|(grn)|(hzl)|(oth)$/ &&
    $passport{"pid"} =~ /^[0-9]{9}$/
} @filledPassports;

print scalar @filledPassports;
print scalar @validPassports;
