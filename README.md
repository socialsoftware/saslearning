# SAS Learning

[![Build Status](https://travis-ci.org/socialsoftware/saslearning.svg?branch=master)](https://travis-ci.org/socialsoftware/saslearning)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/fa77881953384641abdefe91c09c309c)](https://www.codacy.com/app/magicknot/saslearning?utm_source=github.com&utm_medium=referral&utm_content=socialsoftware/saslearning&utm_campaign=badger)
[![Codacy Badge](https://api.codacy.com/project/badge/Coverage/fa77881953384641abdefe91c09c309c)](https://www.codacy.com/app/magicknot/saslearning?utm_source=github.com&utm_medium=referral&utm_content=socialsoftware/saslearning&utm_campaign=Badge_Coverage)

## Description

## API

See [API.md](API.md) for a detailed description about the API provided by SAS Learning.

## Contributing

### Dependencies

In order to have a running version of this project you must setup your machine with:

- Git
- Scala 2.12.4
- SBT 1.1.0-RC1

Now clone the repository to your machine by:

```bash
git clone git@github.com:socialsoftware/saslearning.git
```

### Configuration

### Testing

After having your machine ready to go, all you need to do to run tests is:

```
sbt test
```

### Running

After having your machine setup and your configuration defined all you have to is:

```
$ sbt reStart
``` 

We use [sbt-revolver](https://github.com/spray/sbt-revolver) for starting/stopping
and triggered restart. Please see its documentation for other usage options.
Traditional `sbt run` also works.

