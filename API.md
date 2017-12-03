# SAS Learning API

This document describes the API provided by the application.

## Auth

For the configured OAuth2 Providers, the application will generate an endpoint for the user to register and another one
for the OAuth2 Provider to callback.

### Register

- `GET /{OAUTH2_PROVIDER}/register` → Redirects the user to the authorization page of the OAuth2 provider.

### Callback

- `GET /{OAUTH2_PROVIDER}/callback` → Receives the OAuth2 token for the user and fetches the user profile.

## Users

### List all

### List specific user

### Create new user

## Documents

### List all

### List specific document

### Create new document

## Annotations

### List annotation

### List specific annotation

### Create new annotation
