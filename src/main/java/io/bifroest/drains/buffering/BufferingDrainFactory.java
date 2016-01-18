package io.bifroest.drains.buffering;

import java.util.Collections;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import org.kohsuke.MetaInfServices;

import io.bifroest.commons.boot.interfaces.Environment;
import io.bifroest.commons.statistics.units.SI_PREFIX;
import io.bifroest.commons.statistics.units.TIME_UNIT;
import io.bifroest.commons.statistics.units.parse.TimeUnitParser;
import io.bifroest.drains.Drain;
import io.bifroest.drains.DrainWrapperFactory;

@MetaInfServices
public class BufferingDrainFactory<E extends Environment> implements DrainWrapperFactory<E> {
    @Override
    public String handledType() {
        return "buffered";
    }

    @Override
    public Drain wrap(E environment, Drain inner, JSONObject config, String name) {
        long warnlimit;
        try {
            warnlimit = config.getLong("warnlimit") * 1000;
        } catch (JSONException e) {
            String warn = config.getString("warnlimit");
            warnlimit = (new TimeUnitParser(SI_PREFIX.MILLI, TIME_UNIT.SECOND)).parse(warn).longValue();
        }

        return new BufferingDrain(
                config.getInt("buffersize"),
                warnlimit,
                inner,
                name
        );
    }

    @Override
    public List<Class<? super E>> getRequiredEnvironments() {
        return Collections.<Class<? super E>> emptyList();
    }
}
