<script lang="ts">
  import { onMount } from "svelte";
  import Masonry from "../component/Masonry.svelte";
  import type { CardType } from "$models/cards-link.model";
  import Card from "../component/Card.svelte";

  let apiData: CardType[] = [];
  let itemCount = 2;
  onMount(async () => {
    fetch("/api/login", {
      method: "POST",
      body: JSON.stringify({ email: "giuseppe06@gmail.com", password: "gYyAk$L6ytgYT7Nz" }),
      headers: { "content-type": "application/json" }
    }).then();
    fetch("/api/thoughts")
      .then(response => response.json())
      .then((data) => {
        apiData = data;
        itemCount = data.length;
      }).catch(error => {
      return [];
    });
  });
  const cols = [
    [1500, 5],
    [1024, 3],
    [500, 1],
  ];
</script>
<h1>Welcome to SvelteKit</h1>
<p>Visit <a href="https://kit.svelte.dev">kit.svelte.dev</a> to read the documentation</p>


<Masonry stretchFirst={false} gridGap={'0.75rem'} colWidth={'minmax(Min(20em, 100%), 1fr)'} items={apiData.slice(0,itemCount)}>
  {#each apiData.slice(0,itemCount) as card}
    <Card card="{card}"></Card>
  {/each}
</Masonry>
<style>

</style>