use std::fs::File;
use std::io::prelude::*;
use std::path::Path;

struct Bus {
    bus_id: i64,
    index: i64
}

struct BusPlan {
    time: i64,
    busses: Vec<Bus>
}

fn parse(input: String) -> BusPlan {
    let split_input: Vec<&str> = input.split("\n").collect();
    let mut i: i64 = -1;

    let time: i64 = split_input[0].parse::<i64>().unwrap();
    let values: Vec<Bus> = split_input[1]
        .split(",")
        .map(|val| {
            i += 1;
            match val {
                "x" => None,
                _   => Some(Bus {
                    bus_id: val.parse::<i64>().unwrap(),
                    index:i
                })
            }
        })
        .filter_map(|e| e)
        .collect();

    return BusPlan {
        time: time,
        busses: values
    };
}

fn part1(plan: &BusPlan) {
    let mut time_to_bus: i64 = i64::MAX;
    let mut next_bus: i64 = 0;

    for bus in &plan.busses {
        if bus.bus_id - (plan.time % bus.bus_id) < time_to_bus {
            time_to_bus = bus.bus_id - (plan.time % bus.bus_id);
            next_bus = bus.bus_id;
        }
    }

    println!("{}", time_to_bus * next_bus);
}

fn part2(plan: &BusPlan) {
    let mut increment : i64 = 1;
    let mut earliest_match: i64 = 0;

    for bus in &plan.busses {
        while (earliest_match + bus.index) % bus.bus_id != 0 {
            earliest_match += increment;
        }
        increment *= bus.bus_id;
    }

    if is_valid(plan, earliest_match) {
        println!("{}", earliest_match);
    } else {
        panic!("💩")
    }
}

fn is_valid(plan: &BusPlan, start: i64) -> bool {
    for bus in &plan.busses {
        if (start + bus.index) % bus.bus_id != 0 {
            return false;
        }
    }
    return true;
}

fn solve(input: String) {
    let plan = parse(input);
    part1(&plan);
    part2(&plan);
}

fn main() {
    let path = Path::new("input");
    let display = path.display();

    let mut file = match File::open(&path) {
        Err(why) => panic!("couldn't open {}: {}", display, why),
        Ok(file) => file,
    };

    let mut s = String::new();
    match file.read_to_string(&mut s) {
        Err(why) => panic!("couldn't read {}: {}", display, why),
        Ok(_) => solve(s),
    }
}
